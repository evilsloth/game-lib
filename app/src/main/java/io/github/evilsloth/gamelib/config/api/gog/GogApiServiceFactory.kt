package io.github.evilsloth.gamelib.config.api.gog

import android.content.Context
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.evilsloth.gamelib.R
import io.github.evilsloth.gamelib.accounts.gog.GogAuthTokenRepository
import io.github.evilsloth.gamelib.accounts.gog.GogTokenApiService
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

private const val TAG = "GogApiServiceFactory"

@Singleton
class GogApiServiceFactory @Inject constructor(
    private val gogAuthTokenRepository: GogAuthTokenRepository,
    private val gogTokenApiService: GogTokenApiService,
    @ApplicationContext private val context: Context
) {

    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(Interceptor { chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            requestBuilder.header("Content-Type", "application/json")

            val token = gogAuthTokenRepository.getAccessToken()
            if (token != null) {
                requestBuilder.header("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        })
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .authenticator { _, response ->
            val newAccessToken = refreshToken()
            newAccessToken?.let {
                response.request.newBuilder().header("Authorization", "Bearer $it").build()
            }
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(GogApiConstants.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    private fun refreshToken(): String? {
        val oldToken = gogAuthTokenRepository.getRefreshToken() ?: throw IllegalStateException("Null refresh token")
        val call = gogTokenApiService.getRefreshedToken(refreshToken = oldToken)
        val authTokenResponse = call.execute()
        val accessToken = authTokenResponse.body()?.access_token
        val refreshToken = authTokenResponse.body()?.refresh_token

        if (!authTokenResponse.isSuccessful || accessToken == null || refreshToken == null) {
            Log.e(TAG, "refreshToken error: ${authTokenResponse.errorBody()}, token: $accessToken")
            gogAuthTokenRepository.clearTokens()
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, context.getString(R.string.gog_logged_out), Toast.LENGTH_LONG).show()
            }

            return null
        }

        gogAuthTokenRepository.saveTokens(accessToken, refreshToken)
        return accessToken
    }

}