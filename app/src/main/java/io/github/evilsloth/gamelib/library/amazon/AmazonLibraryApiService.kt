package io.github.evilsloth.gamelib.library.amazon

import io.github.evilsloth.gamelib.library.amazon.entitlements.AmazonEntitlementsRequest
import io.github.evilsloth.gamelib.library.amazon.entitlements.AmazonEntitlementsResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AmazonLibraryApiService {

    @Headers("X-Amz-Target: com.amazon.animusdistributionservice.entitlement.AnimusEntitlementsService.GetEntitlements")
    @POST("distribution/entitlements")
    suspend fun getUserGames(@Body request: AmazonEntitlementsRequest): AmazonEntitlementsResponse

}
