package io.github.evilsloth.gamelib.config.api.amazon

import android.content.Context
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.evilsloth.gamelib.R
import io.github.evilsloth.gamelib.accounts.amazon.AmazonAuthTokenRepository
import io.github.evilsloth.gamelib.accounts.amazon.AmazonTokenApiService
import io.github.evilsloth.gamelib.accounts.amazon.refresh.AmazonRefreshTokenRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "AmazonApiServiceFactory"

@Singleton
class AmazonApiServiceFactory @Inject constructor(
    private val amazonAuthTokenRepository: AmazonAuthTokenRepository,
    private val amazonTokenApiService: AmazonTokenApiService,
    @ApplicationContext private val context: Context
) {

    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(Interceptor { chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()
                .header("Content-Type", "application/json")
                .header("UserAgent", "com.amazon.agslauncher.win/3.0.9202.1")
                .header("Content-Encoding", "amz-1.0")

            val token = amazonAuthTokenRepository.getAccessToken()
            if (token != null) {
                requestBuilder.header("x-amzn-token", "$token")
            }

            chain.proceed(requestBuilder.build())
        })
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .authenticator { _, response ->
            val newAccessToken = refreshToken()
            newAccessToken?.let {
                response.request.newBuilder().header("x-amzn-token", it).build()
            }
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(AmazonApiConstants.GAMES_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    private fun refreshToken(): String? {
        val refreshToken = amazonAuthTokenRepository.getRefreshToken() ?: throw IllegalStateException("Null refresh token")
        val call = amazonTokenApiService.refreshTokens(AmazonRefreshTokenRequest(source_token = refreshToken))
        val authTokenResponse = call.execute()
        val accessToken = authTokenResponse.body()?.access_token

        if (!authTokenResponse.isSuccessful || accessToken == null) {
            Log.e(TAG, "refreshToken error: ${authTokenResponse.errorBody()}, token: $accessToken")
            amazonAuthTokenRepository.clearTokens()
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, context.getString(R.string.amazon_logged_out), Toast.LENGTH_LONG).show()
            }

            return null
        }

        amazonAuthTokenRepository.saveAccessToken(accessToken)
        return accessToken
    }

}