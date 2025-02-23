package io.github.evilsloth.gamelib.accounts.steam

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

const val API_KEY = "API_KEY"
const val STEAM_ID_KEY = "STEAM_ID"

class SteamAuthTokenRepository @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPref = context.getSharedPreferences("STEAM_AUTH_TOKEN_PREFS", Context.MODE_PRIVATE)

    fun saveTokens(apiKey: String, steamId: String) {
        with (sharedPref.edit()) {
            putString(API_KEY, apiKey)
            putString(STEAM_ID_KEY, steamId)
            apply()
        }
    }

    fun getApiKey(): String? {
        return sharedPref.getString(API_KEY, null)
    }

    fun getSteamId(): String? {
        return sharedPref.getString(STEAM_ID_KEY, null)
    }

    fun clearTokens() {
        with (sharedPref.edit()) {
            clear()
            apply()
        }
    }

}