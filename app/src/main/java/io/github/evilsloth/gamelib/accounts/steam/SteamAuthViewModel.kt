package io.github.evilsloth.gamelib.accounts.steam

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SteamAuthViewModel @Inject constructor(
    private val steamAuthTokenRepository: SteamAuthTokenRepository
) : ViewModel() {

    val authenticated = MutableStateFlow(false)

    fun refreshStatus() {
        authenticated.value = steamAuthTokenRepository.getApiKey() != null
    }

    fun saveLoginData(apiKey: String, steamId: String) {
        authenticated.value = true
        steamAuthTokenRepository.saveTokens(apiKey, steamId)
    }

    fun logOut() {
        steamAuthTokenRepository.clearTokens()
        authenticated.value = false
    }

}