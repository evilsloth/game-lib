package io.github.evilsloth.gamelib.accounts.amazon

import io.github.evilsloth.gamelib.accounts.amazon.refresh.AmazonRefreshTokenRequest
import io.github.evilsloth.gamelib.accounts.amazon.refresh.AmazonRefreshTokenResponse
import io.github.evilsloth.gamelib.accounts.amazon.register.AmazonRegisterDeviceRequest
import io.github.evilsloth.gamelib.accounts.amazon.register.AmazonRegisterDeviceResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AmazonTokenApiService {

    @POST("auth/register")
    suspend fun registerDevice(@Body request: AmazonRegisterDeviceRequest): AmazonRegisterDeviceResponse

    @POST("auth/token")
    fun refreshTokens(@Body request: AmazonRefreshTokenRequest): Call<AmazonRefreshTokenResponse>

}
