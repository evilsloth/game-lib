package io.github.evilsloth.gamelib.accounts.gog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "GogAuthViewModel"

@HiltViewModel
class GogAuthViewModel @Inject constructor(
    private val gogTokenApiService: GogTokenApiService,
    private val gogAuthTokenRepository: GogAuthTokenRepository
) : ViewModel() {

    val authenticated = MutableStateFlow(false)
    val tokenSaveLoading = MutableStateFlow(false)

    fun refreshStatus() {
        authenticated.value = gogAuthTokenRepository.getAccessToken() != null
    }

    fun getAndSaveToken(code: String, onSuccess: () -> Unit, onError: () -> Unit) {
        tokenSaveLoading.value = true

        viewModelScope.launch {
            try {
                val response = gogTokenApiService.getNewToken(code)
                gogAuthTokenRepository.saveTokens(response.access_token, response.refresh_token)
                authenticated.value = true
                onSuccess()
            } catch (e: Exception) {
                Log.e(TAG, "getAndSaveToken", e)
                gogAuthTokenRepository.clearTokens()
                authenticated.value = false
                onError()
            } finally {
                tokenSaveLoading.value = false
            }
        }
    }

    fun logOut() {
        gogAuthTokenRepository.clearTokens()
        authenticated.value = false
    }

}