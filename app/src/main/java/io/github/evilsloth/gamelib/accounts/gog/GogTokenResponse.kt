package io.github.evilsloth.gamelib.accounts.gog

data class GogTokenResponse(
    val expires_in: Long,
    val scope: String,
    val token_type: String,
    val access_token: String,
    val user_id: String,
    val refresh_token: String,
    val session_id: String
)
