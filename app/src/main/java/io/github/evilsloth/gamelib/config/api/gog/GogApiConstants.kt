package io.github.evilsloth.gamelib.config.api.gog

object GogApiConstants {
    const val CLIENT_ID = "46899977096215655"
    const val CLIENT_SECRET = "9d85c43b1482497dbbce61f6e4aa173a433796eeae2ca8c5f6129f2dc4de46d9"

    const val LOGIN_PAGE_URL = "https://auth.gog.com/auth" +
            "?client_id=$CLIENT_ID" +
            "&redirect_uri=https%3A%2F%2Fembed.gog.com%2Fon_login_success%3Forigin%3Dclient" +
            "&response_type=code" +
            "&layout=client2"
    const val AUTH_SUCCESS_URL = "https://embed.gog.com/on_login_success"
    const val AUTH_API_URL = "https://auth.gog.com"
    const val API_URL = "https://embed.gog.com"
}