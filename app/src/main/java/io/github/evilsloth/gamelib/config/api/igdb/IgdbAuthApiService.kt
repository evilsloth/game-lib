package io.github.evilsloth.gamelib.config.api.igdb

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface IgdbAuthApiService {

    @POST("oauth2/token")
    fun getToken(
        @Query("client_id") clientId: String = IgdbApiConstants.CLIENT_ID,
        @Query("client_secret") clientSecret: String = IgdbApiConstants.CLIENT_SECRET,
        @Query("grant_type") grantType: String = "client_credentials"
    ): Call<IgdbTokenResponse>

}
