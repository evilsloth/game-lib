package io.github.evilsloth.gamelib.accounts.amazon.login

data class AmazonLoginData(
    val url: String,
    val clientId: String,
    val deviceSerial: String,
    val codeVerifier: String
)