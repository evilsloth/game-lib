package io.github.evilsloth.gamelib.config.api.egs

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EgsAuthApiServiceFactory @Inject constructor() {

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addNetworkInterceptor(Interceptor { chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            requestBuilder.header("Authorization", Credentials.basic(EgsApiConstants.CLIENT_ID, EgsApiConstants.CLIENT_SECRET))
            chain.proceed(requestBuilder.build())
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(EgsApiConstants.AUTH_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

}