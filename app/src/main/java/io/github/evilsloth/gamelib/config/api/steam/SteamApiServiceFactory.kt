package io.github.evilsloth.gamelib.config.api.steam

import android.content.Context
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.evilsloth.gamelib.R
import io.github.evilsloth.gamelib.accounts.steam.SteamAuthTokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "SteamApiServiceFactory"

@Singleton
class SteamApiServiceFactory @Inject constructor(
    steamAuthTokenRepository: SteamAuthTokenRepository,
    @ApplicationContext context: Context
) {

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .authenticator { _, response ->
            Log.e(TAG, "Steam auth error: ${response.body}")
            steamAuthTokenRepository.clearTokens()
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, context.getString(R.string.steam_logged_out), Toast.LENGTH_LONG).show()
            }
            throw IllegalStateException("Steam returned 401 - invalid key?")
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(SteamApiConstants.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

}