package io.github.evilsloth.gamelib.accounts.amazon

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.evilsloth.gamelib.accounts.amazon.login.AmazonLoginData
import io.github.evilsloth.gamelib.accounts.amazon.login.AmazonLoginDataGenerator
import io.github.evilsloth.gamelib.accounts.amazon.register.AmazonAuthData
import io.github.evilsloth.gamelib.accounts.amazon.register.AmazonRegisterDeviceRequest
import io.github.evilsloth.gamelib.accounts.amazon.register.AmazonRegistrationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest
import javax.inject.Inject

private const val TAG = "AmazonAuthViewModel"

@HiltViewModel
class AmazonAuthViewModel @Inject constructor(
    private val amazonAuthTokenRepository: AmazonAuthTokenRepository,
    private val amazonTokenApiService: AmazonTokenApiService
) : ViewModel() {

    val authenticated = MutableStateFlow(false)
    val tokenSaveLoading = MutableStateFlow(false)

    var amazonLoginData: AmazonLoginData? = null

    fun refreshStatus() {
        authenticated.value = amazonAuthTokenRepository.getAccessToken() != null
    }

    fun generateLoginData(): AmazonLoginData {
        val data = AmazonLoginDataGenerator.generate()
        amazonLoginData = data
        return data
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getAndSaveToken(code: String, onSuccess: () -> Unit, onError: () -> Unit) {
        tokenSaveLoading.value = true

        viewModelScope.launch {
            try {
                val authData = amazonLoginData ?: throw IllegalStateException("amazonLoginData cannot be null!")
                val request = AmazonRegisterDeviceRequest(
                    auth_data = AmazonAuthData(
                        authorization_code = code,
                        client_id = authData.clientId,
                        code_verifier = authData.codeVerifier
                    ),
                    registration_data = AmazonRegistrationData(
                        device_serial = authData.deviceSerial
                    )
                )
                val response = amazonTokenApiService.registerDevice(request)
                val tokens = response.response.success.tokens.bearer
                val serial = response.response.success.extensions.device_info.device_serial_number
                val serialHash = MessageDigest.getInstance("SHA-256").digest(serial.toByteArray()).toHexString(HexFormat.UpperCase)
                amazonAuthTokenRepository.saveTokens(tokens.access_token, tokens.refresh_token, serialHash)
                authenticated.value = true
                onSuccess()
            } catch (e: Exception) {
                Log.e(TAG, "getAndSaveToken", e)
                amazonAuthTokenRepository.clearTokens()
                authenticated.value = false
                onError()
            } finally {
                tokenSaveLoading.value = false
            }
        }
    }

    fun logOut() {
        amazonAuthTokenRepository.clearTokens()
        authenticated.value = false
    }

}