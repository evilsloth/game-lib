package io.github.evilsloth.gamelib.accounts.egs

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN"
const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN"

class EgsAuthTokenRepository @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPref = context.getSharedPreferences("EGS_AUTH_TOKEN_PREFS", Context.MODE_PRIVATE)

    fun saveTokens(accessToken: String, refreshToken: String) {
        with (sharedPref.edit()) {
            putString(ACCESS_TOKEN_KEY, accessToken)
            putString(REFRESH_TOKEN_KEY, refreshToken)
            apply()
        }
    }

    fun getAccessToken(): String? {
        return sharedPref.getString(ACCESS_TOKEN_KEY, null)
    }

    fun getRefreshToken(): String? {
        return sharedPref.getString(REFRESH_TOKEN_KEY, null)
    }

    fun clearTokens() {
        with (sharedPref.edit()) {
            clear()
            apply()
        }
    }

}