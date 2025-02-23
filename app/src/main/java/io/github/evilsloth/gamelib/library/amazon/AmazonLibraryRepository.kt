package io.github.evilsloth.gamelib.library.amazon

import io.github.evilsloth.gamelib.accounts.amazon.AmazonAuthTokenRepository
import io.github.evilsloth.gamelib.games.GameRepository
import io.github.evilsloth.gamelib.library.amazon.entitlements.AmazonEntitlement
import io.github.evilsloth.gamelib.library.amazon.entitlements.AmazonEntitlementsRequest
import io.github.evilsloth.gamelib.library.model.LibraryItem
import java.util.regex.Pattern
import javax.inject.Inject

private const val IDS_CHUNK_SIZE = 100

class AmazonLibraryRepository @Inject constructor(
    private val amazonLibraryApiService: AmazonLibraryApiService,
    private val amazonAuthTokenRepository: AmazonAuthTokenRepository,
    private val gameRepository: GameRepository
) {

    private val steamIdPattern = Pattern.compile("/(\\d+)(?=/|$)")

    suspend fun getUserGames(): List<LibraryItem> {
        if (amazonAuthTokenRepository.getAccessToken() == null) {
            return listOf()
        }

        val deviceSerial = amazonAuthTokenRepository.getDeviceSerial()!!
        var nextToken: String? = null
        var entitlements = listOf<AmazonEntitlement>()

        do {
            val response = amazonLibraryApiService.getUserGames(
                AmazonEntitlementsRequest(
                    hardwareHash = deviceSerial,
                    nextToken = nextToken
                )
            )
            entitlements += response.entitlements
            nextToken = response.nextToken
        } while (nextToken != null)

        val steamIdsByProductIds = entitlements
            .map {
                val url = it.product.productDetail.details.websites.STEAM?.let {
                    val matcher = steamIdPattern.matcher(it)
                    if (matcher.find()) matcher.group(1) else null
                }
                it.product.id to url
            }
            .toMap()

        val dbGames = steamIdsByProductIds.entries.chunked(IDS_CHUNK_SIZE).flatMap { idsEntriesChunk ->
            val steamIds = idsEntriesChunk.mapNotNull { it.value }
            val productsIds = idsEntriesChunk.map { it.key }

            gameRepository.getGames(steamIds = steamIds, amazonIds = productsIds)
        }

        val dbGamesByAmazonId = dbGames.associateBy { it.ids.amazonId }
        val dbGamesBySteamId = dbGames.associateBy { it.ids.steamId }

        return entitlements.map {
            var dbGame = dbGamesByAmazonId[it.product.id]

            if (dbGame == null) {
                val steamId = steamIdsByProductIds[it.product.id]
                dbGame = if (steamId != null) dbGamesBySteamId[steamId] else null
            }

            LibraryItem(
                igdbId = dbGame?.ids?.igdbId,
                externalId = it.product.id,
                name = dbGame?.name ?: it.product.title,
                platform = LibraryItem.Platform.AMAZON,
                url = dbGame?.url,
                coverUrl = dbGame?.coverUrl,
                thumbnailUrl = dbGame?.thumbnailUrl
            )
        }
    }

}