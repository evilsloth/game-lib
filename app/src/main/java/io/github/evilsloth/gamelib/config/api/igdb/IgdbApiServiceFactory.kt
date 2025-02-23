package io.github.evilsloth.gamelib.config.api.igdb

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "IgdbApiServiceFactory"
private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN"

@Singleton
class IgdbApiServiceFactory @Inject constructor(@ApplicationContext private val context: Context) {

    private val sharedPref = context.getSharedPreferences("IGDB_AUTH_TOKEN_PREFS", Context.MODE_PRIVATE)

    private val client = OkHttpClient.Builder()
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addNetworkInterceptor(Interceptor { chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            requestBuilder.header("Content-Type", "application/json")
            requestBuilder.header("Client-ID", IgdbApiConstants.CLIENT_ID)

            val token = getAccessToken()
            if (token != null) {
                requestBuilder.header("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        })
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .authenticator { _, response ->
            val newAccessToken = requestNewToken()
            newAccessToken?.let {
                response.request.newBuilder().header("Authorization", "Bearer $it").build()
            }
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(IgdbApiConstants.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    private fun requestNewToken(): String? {
        val authRetrofit = Retrofit.Builder()
            .baseUrl(IgdbApiConstants.AUTH_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        val authService = authRetrofit.create(IgdbAuthApiService::class.java)
        val call = authService.getToken()
        val authTokenResponse = call.execute()
        val accessToken = authTokenResponse.body()?.access_token

        if (!authTokenResponse.isSuccessful || accessToken == null) {
            Log.e(TAG, "getToken error: ${authTokenResponse.errorBody()}, token: $accessToken")
            return null
        }

        saveAccessToken(accessToken)
        return accessToken
    }

    private fun saveAccessToken(accessToken: String) {
        with (sharedPref.edit()) {
            putString(ACCESS_TOKEN_KEY, accessToken)
            apply()
        }
    }

    private fun getAccessToken(): String? {
        return sharedPref.getString(ACCESS_TOKEN_KEY, null)
    }

}