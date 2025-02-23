package io.github.evilsloth.gamelib.accounts.amazon.register

data class AmazonDeviceSuccessResponse(
    val extensions: AmazonDeviceExtensions,
    val tokens: AmazonDeviceTokens,
    val customer_id: String
)
