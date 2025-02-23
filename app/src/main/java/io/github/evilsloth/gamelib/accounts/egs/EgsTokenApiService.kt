package io.github.evilsloth.gamelib.accounts.egs

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EgsTokenApiService {

    @FormUrlEncoded
    @POST("account/api/oauth/token")
    suspend fun getNewToken(
        @Field("code") code: String,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("token_type") tokenType: String = "eg1"
    ): EgsTokenResponse

    @FormUrlEncoded
    @POST("account/api/oauth/token")
    fun getRefreshedToken(
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("token_type") tokenType: String = "eg1"
    ): Call<EgsTokenResponse>

}
