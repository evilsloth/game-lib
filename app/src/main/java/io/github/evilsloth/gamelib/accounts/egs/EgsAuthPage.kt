package io.github.evilsloth.gamelib.accounts.egs

import android.content.Context
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import io.github.evilsloth.gamelib.R
import io.github.evilsloth.gamelib.common.ui.SubPage
import io.github.evilsloth.gamelib.config.api.egs.EgsApiConstants
import java.util.regex.Matcher
import java.util.regex.Pattern


@Composable
fun EgsAuthPage(
    navController: NavHostController,
    egsAuthViewModel: EgsAuthViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val context = LocalContext.current
    val loading by egsAuthViewModel.tokenSaveLoading.collectAsState()

    SubPage(title = stringResource(R.string.log_in_to_egs), navController = navController) {
        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            AndroidView(factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    settings.javaScriptEnabled = true
                    settings.loadWithOverviewMode = true
                    addJavascriptInterface(WebViewJsInterface(onAuthenticated = { code ->
                        if (code == null) {
                            showAuthError(context)
                            navController.popBackStack()
                        } else {
                            egsAuthViewModel.getAndSaveToken(
                                code = code,
                                onSuccess = { navController.popBackStack() },
                                onError = {
                                    showAuthError(context)
                                    navController.popBackStack()
                                }
                            )
                        }
                    }), "JSBridge")
                    webViewClient = CustomWebViewClient()
                }
            }, update = {
                it.loadUrl(EgsApiConstants.LOGIN_PAGE_URL)
            })
        }
    }
}

private class CustomWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        return false
    }

    override fun onPageFinished(webView: WebView, url: String) {
        if (url.startsWith(EgsApiConstants.AUTH_SUCCESS_URL)) {
            webView.loadUrl("javascript:JSBridge.getContent(document.getElementsByTagName('html')[0].innerHTML);")
        }
    }
}

private fun showAuthError(context: Context) {
    Toast.makeText(context, context.getString(R.string.authentication_failed), Toast.LENGTH_LONG).show()
}

private class WebViewJsInterface(private val onAuthenticated: (code: String?) -> Unit) {

    private var done = false;

    @JavascriptInterface
    fun getContent(html: String?) {
        val pattern: Pattern = Pattern.compile("\"authorizationCode\":\"([^\"]+)\"")
        val matcher: Matcher = pattern.matcher(html ?: "")

        val code = if (matcher.find()) {
            matcher.group(1)
        } else {
            null
        }

        if (!done) {
            done = true
            onAuthenticated(code)
        }
    }
}
