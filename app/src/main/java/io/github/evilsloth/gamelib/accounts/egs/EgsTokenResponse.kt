package io.github.evilsloth.gamelib.accounts.egs

data class EgsTokenResponse(
    val access_token: String,
    val expires_in: Long,
    val token_type: String,
    val refresh_token: String,
    val refresh_expires: Long
)
