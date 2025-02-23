package io.github.evilsloth.gamelib.accounts.amazon.refresh

data class AmazonRefreshTokenResponse(
    val access_token: String,
    val expires_in: Long
)
