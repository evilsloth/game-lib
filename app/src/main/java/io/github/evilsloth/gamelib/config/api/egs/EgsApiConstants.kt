package io.github.evilsloth.gamelib.config.api.egs

object EgsApiConstants {
    const val CLIENT_ID = "34a02cf8f4414e29b15921876da36f9a"
    const val CLIENT_SECRET = "daafbccc737745039dffe53d94fc76cf"

    const val LOGIN_PAGE_URL = "https://www.epicgames.com/id/login" +
            "?redirect_uri=https%3A%2F%2Fwww.epicgames.com%2Fid%2Fapi%2Fredirect%3FclientId%3D$CLIENT_ID%26responseType%3Dcode"
    const val AUTH_SUCCESS_URL = "https://www.epicgames.com/id/api/redirect"
    const val AUTH_API_URL = "https://account-public-service-prod03.ol.epicgames.com"
    const val LIBRARY_API_URL = "https://library-service.live.use1a.on.epicgames.com"
    const val PRODUCT_API_URL = "https://egs-platform-service.store.epicgames.com/api/v1/egs/"
}