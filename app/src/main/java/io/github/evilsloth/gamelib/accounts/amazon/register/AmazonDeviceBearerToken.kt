package io.github.evilsloth.gamelib.accounts.amazon.register

data class AmazonDeviceBearerToken(
    val access_token: String,
    val refresh_token: String,
    val expires_in: Long
)
