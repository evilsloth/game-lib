package io.github.evilsloth.gamelib.accounts.amazon

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
import io.github.evilsloth.gamelib.config.api.amazon.AmazonApiConstants

@Composable
fun AmazonAuthPage(
    navController: NavHostController,
    amazonAuthViewModel: AmazonAuthViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
) {
    val context = LocalContext.current
    val loading by amazonAuthViewModel.tokenSaveLoading.collectAsState()

    SubPage(title = stringResource(R.string.log_in_to_amazon), navController = navController) {
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
                            amazonAuthViewModel.getAndSaveToken(
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
                val authData = amazonAuthViewModel.generateLoginData()
                it.loadUrl(authData.url)
            })
        }
    }
}

private class CustomWebViewClient(
    private val onAuthenticated: (code: String?) -> Unit
) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        if (!request.url.toString().startsWith(AmazonApiConstants.AUTHORIZATION_URL)) {
            onAuthenticated(request.url.getQueryParameter("openid.oa2.authorization_code"))
            return true
        }
        return false
    }
}

private fun showAuthError(context: Context) {
    Toast.makeText(context, context.getString(R.string.authentication_failed), Toast.LENGTH_LONG).show()
}
