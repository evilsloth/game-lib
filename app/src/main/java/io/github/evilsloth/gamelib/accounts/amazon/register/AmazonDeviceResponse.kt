package io.github.evilsloth.gamelib.accounts.amazon.register

data class AmazonDeviceResponse(
    val success: AmazonDeviceSuccessResponse,
    val request_id: String
)
