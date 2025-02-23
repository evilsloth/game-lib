package io.github.evilsloth.gamelib.library.amazon.entitlements

data class AmazonEntitlementsRequest(
    val Operation: String = "GetEntitlements",
    val clientId: String = "Sonic",
    val syncPoint: String? = null,
    val nextToken: String? = null,
    val maxResults: Int = 1000,
    val productIdFilter: String? = null,
    val keyId: String = "d5dc8b8b-86c8-4fc4-ae93-18c0def5314d",
    val hardwareHash: String
)
