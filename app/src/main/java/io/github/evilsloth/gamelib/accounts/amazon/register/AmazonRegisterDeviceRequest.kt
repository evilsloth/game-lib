package io.github.evilsloth.gamelib.accounts.amazon.register

data class AmazonRegisterDeviceRequest(
    val auth_data: AmazonAuthData,
    val registration_data: AmazonRegistrationData,
    val requested_extensions: List<String> = listOf("customer_info", "device_info"),
    val requested_token_type: List<String> = listOf("bearer", "mac_dms"),
    val user_context_map: Map<String, String> = mapOf()
)
