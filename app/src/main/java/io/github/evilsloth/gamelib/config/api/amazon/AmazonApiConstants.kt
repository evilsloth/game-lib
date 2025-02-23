package io.github.evilsloth.gamelib.config.api.amazon

import android.util.Base64
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.UUID

object AmazonApiConstants {
    const val AUTHORIZATION_URL = "https://www.amazon.com/ap"
    const val API_URL = "https://api.amazon.com"
    const val GAMES_API_URL = "https://gaming.amazon.com/api/"
}