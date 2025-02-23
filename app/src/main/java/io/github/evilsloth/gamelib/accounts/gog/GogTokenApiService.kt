package io.github.evilsloth.gamelib.accounts.gog

import io.github.evilsloth.gamelib.config.api.gog.GogApiConstants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GogTokenApiService {

    @GET("token")
    suspend fun getNewToken(
        @Query("code") code: String,
        @Query("grant_type") grantType: String = "authorization_code",
        @Query("client_id") clientId: String = GogApiConstants.CLIENT_ID,
        @Query("client_secret") clientSecret: String = GogApiConstants.CLIENT_SECRET,
        @Query("redirect_uri") redirectUri: String = "https://embed.gog.com/on_login_success?origin=client"
    ): GogTokenResponse

    @GET("token")
    fun getRefreshedToken(
        @Query("refresh_token") refreshToken: String,
        @Query("grant_type") grantType: String = "refresh_token",
        @Query("client_id") clientId: String = GogApiConstants.CLIENT_ID,
        @Query("client_secret") clientSecret: String = GogApiConstants.CLIENT_SECRET
    ): Call<GogTokenResponse>

}
