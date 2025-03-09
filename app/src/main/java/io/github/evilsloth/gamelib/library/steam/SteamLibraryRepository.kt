package io.github.evilsloth.gamelib.library.steam

import io.github.evilsloth.gamelib.accounts.steam.SteamAuthTokenRepository
import io.github.evilsloth.gamelib.games.GameRepository
import io.github.evilsloth.gamelib.library.model.LibraryItem
import javax.inject.Inject

private const val IDS_CHUNK_SIZE = 100

class SteamLibraryRepository @Inject constructor(
    private val steamLibraryApiService: SteamLibraryApiService,
    private val steamAuthTokenRepository: SteamAuthTokenRepository,
    private val gameRepository: GameRepository
) {

    suspend fun getUserGames(): List<LibraryItem> {
        val apiKey = steamAuthTokenRepository.getApiKey()
        val steamId = steamAuthTokenRepository.getSteamId()
        if (apiKey == null || steamId == null) {
            return listOf()
        }

        val response = steamLibraryApiService.getUserGames(
            apiKey = apiKey,
            steamId = steamId
        )

        val gamesIds = response.response.games.map { it.appid.toString() }

        if (gamesIds.isEmpty()) {
            return listOf()
        }

        val dbGames = gamesIds.chunked(IDS_CHUNK_SIZE).flatMap { idsChunk ->
            gameRepository.getGames(steamIds = idsChunk)
        }
        val dbGamesBySteamId = dbGames.associateBy { it.ids.steamId }

        return response.response.games
            .map {
                val dbGame = dbGamesBySteamId[it.appid.toString()]

                LibraryItem(
                    igdbId = dbGame?.ids?.igdbId,
                    externalId = it.appid.toString(),
                    name = dbGame?.name ?: it.name,
                    platform = LibraryItem.Platform.STEAM,
                    url = dbGame?.url,
                    rating = dbGame?.rating,
                    userRating = dbGame?.userRating,
                    coverUrl = dbGame?.coverUrl,
                    thumbnailUrl = dbGame?.thumbnailUrl
                )
            }
    }

}