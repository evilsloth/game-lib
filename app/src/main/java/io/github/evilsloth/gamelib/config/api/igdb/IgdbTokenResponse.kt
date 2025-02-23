package io.github.evilsloth.gamelib.config.api.igdb

data class IgdbTokenResponse (
    val access_token: String,
    val expires_in: Long,
    val token_type: String
)