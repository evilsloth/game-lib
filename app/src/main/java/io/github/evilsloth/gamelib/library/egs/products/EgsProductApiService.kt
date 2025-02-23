package io.github.evilsloth.gamelib.library.egs.products

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EgsProductApiService {

    @GET("products/{productId}")
    suspend fun getProductDetails(
        @Path("productId") productId: String,
        @Query("country") country: String = "US",
        @Query("locale") locale: String = "en",
        @Query("store") store: String = "EGS"
    ): EgsProductDetailsResponse

}
