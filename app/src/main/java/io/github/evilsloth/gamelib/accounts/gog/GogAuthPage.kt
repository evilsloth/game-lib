package io.github.evilsloth.gamelib.accounts.gog

import android.content.Context
import android.view.ViewGroup
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
import io.github.evilsloth.gamelib.config.api.gog.GogApiConstants

@Composable
fun GogAuthPage(
    navController: NavHostController,
    gogAuthViewModel: GogAuthViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val context = LocalContext.current
    val loading by gogAuthViewModel.tokenSaveLoading.collectAsState()

    SubPage(title = stringResource(R.string.log_in_to_gog), navController = navController) {
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
                    webViewClient = CustomWebViewClient(onAuthenticated = { code ->
                        if (code == null) {
                            showAuthError(context)
                            navController.popBackStack()
                        } else {
                            gogAuthViewModel.getAndSaveToken(
                                code = code,
                                onSuccess = { navController.popBackStack() },
                                onError = {
                                    showAuthError(context)
                                    navController.popBackStack()
                                }
                            )
                        }
                    })
                }
            }, update = {
                it.loadUrl(GogApiConstants.LOGIN_PAGE_URL)
            })
        }
    }
}

private class CustomWebViewClient(
    private val onAuthenticated: (code: String?) -> Unit
) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        if (request.url.toString().startsWith(GogApiConstants.AUTH_SUCCESS_URL)) {
            onAuthenticated(request.url.getQueryParameter("code"))
            return true
        }
        return false
    }
}

private fun showAuthError(context: Context) {
    Toast.makeText(context, context.getString(R.string.authentication_failed), Toast.LENGTH_LONG).show()
}
