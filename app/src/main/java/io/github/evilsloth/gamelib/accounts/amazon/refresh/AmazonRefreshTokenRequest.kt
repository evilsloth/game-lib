package io.github.evilsloth.gamelib.accounts.amazon.refresh

data class AmazonRefreshTokenRequest(
    val source_token: String,
    val source_token_type: String = "refresh_token",
    val requested_token_type: String = "access_token",
    val app_name: String = "AGSLauncher for Windows",
    val app_version: String = "1.0.0"
)
