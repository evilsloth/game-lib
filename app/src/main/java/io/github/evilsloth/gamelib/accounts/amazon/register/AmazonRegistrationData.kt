package io.github.evilsloth.gamelib.accounts.amazon.register

data class AmazonRegistrationData(
    val app_name: String = "AGSLauncher for Windows",
    val app_version: String = "1.0.0",
    val device_model: String = "Windows",
    val device_name: String? = null,
    val device_serial: String,
    val device_type: String = "A2UMVHOX7UP4V7",
    val domain: String = "Device",
    val os_version: String = "10.0.19044.0"
)
