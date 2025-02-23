package io.github.evilsloth.gamelib.library.amazon.entitlements

data class AmazonEntitlementsResponse(
    val entitlements: List<AmazonEntitlement>,
    val nextToken: String?
)
