package io.github.evilsloth.gamelib.accounts.amazon.register

data class AmazonAuthData(
    val authorization_code: String,
    val client_domain: String = "DeviceLegacy",
    val client_id: String,
    val code_algorithm: String = "SHA-256",
    val code_verifier: String,
    val use_global_authentication: Boolean = false
)
