package io.github.evilsloth.gamelib.accounts.egs

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "EgsAuthViewModel"

@HiltViewModel
class EgsAuthViewModel @Inject constructor(
    private val egsTokenApiService: EgsTokenApiService,
    private val egsAuthTokenRepository: EgsAuthTokenRepository
) : ViewModel() {

    val authenticated = MutableStateFlow(false)
    val tokenSaveLoading = MutableStateFlow(false)

    fun refreshStatus() {
        authenticated.value = egsAuthTokenRepository.getAccessToken() != null
    }

    fun getAndSaveToken(code: String, onSuccess: () -> Unit, onError: () -> Unit) {
        tokenSaveLoading.value = true

        viewModelScope.launch {
            try {
                val response = egsTokenApiService.getNewToken(code)
                egsAuthTokenRepository.saveTokens(response.access_token, response.refresh_token)
                authenticated.value = true
                onSuccess()
            } catch (e: Exception) {
                Log.e(TAG, "getAndSaveToken", e)
                egsAuthTokenRepository.clearTokens()
                authenticated.value = false
                onError()
            } finally {
                tokenSaveLoading.value = false
            }
        }
    }

    fun logOut() {
        egsAuthTokenRepository.clearTokens()
        authenticated.value = false
    }

}