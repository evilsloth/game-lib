package io.github.evilsloth.gamelib.accounts.amazon.login

import android.util.Base64
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.UUID

object AmazonLoginDataGenerator {

    fun generate(): AmazonLoginData {
        val randomValue = ByteArray(32)
        SecureRandom.getInstanceStrong().nextBytes(randomValue)
        val codeVerifier = Base64.encode(randomValue, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        val deviceSerial = UUID.randomUUID().toString().uppercase().replace("-", "")
        val clientId = generateClientId(deviceSerial)

        val loginUrl = "https://www.amazon.com/ap/signin" +
                "?openid.ns=" + urlEncode("http://specs.openid.net/auth/2.0") +
                "&openid.claimed_id=" + urlEncode("http://specs.openid.net/auth/2.0/identifier_select") +
                "&openid.identity=" + urlEncode("http://specs.openid.net/auth/2.0/identifier_select") +
                "&openid.mode=checkid_setup" +
                "&openid.oa2.scope=device_auth_access" +
                "&openid.ns.oa2=" + urlEncode("http://www.amazon.com/ap/ext/oauth/2") +
                "&openid.oa2.response_type=code" +
                "&openid.claimed_id=code" +
                "&openid.oa2.code_challenge_method=S256" +
                "&openid.oa2.client_id=" + urlEncode("device:${clientId}") +
                "&language=en_US" +
                "&marketPlaceId=ATVPDKIKX0DER" +
                "&openid.return_to=" + urlEncode("https://www.amazon.com") +
                "&openid.pape.max_auth_age=0" +
                "&openid.assoc_handle=amzn_sonic_games_launcher" +
                "&pageId=amzn_sonic_games_launcher" +
                "&openid.oa2.code_challenge=" + getChallenge(codeVerifier)

        return AmazonLoginData(
            url = loginUrl,
            clientId = clientId,
            deviceSerial = deviceSerial,
            codeVerifier = codeVerifier.toString(Charsets.UTF_8)
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun generateClientId(serial: String): String {
        val serialEx = "${serial}#A2UMVHOX7UP4V7"
        return serialEx.toByteArray().toHexString()
    }

    private fun getChallenge(codeVerifier: ByteArray): String {
        val hash = MessageDigest.getInstance("SHA-256").digest(codeVerifier)
        return Base64.encode(hash, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP).toString(Charsets.UTF_8)
    }

    private fun urlEncode(value: String): String {
        return URLEncoder.encode(value, "utf-8")
    }

}