package io.github.evilsloth.gamelib.library.gog

import io.github.evilsloth.gamelib.accounts.gog.GogAuthTokenRepository
import io.github.evilsloth.gamelib.games.GameRepository
import io.github.evilsloth.gamelib.library.model.LibraryItem
import javax.inject.Inject

private const val IDS_CHUNK_SIZE = 100

class GogLibraryRepository @Inject constructor(
    private val gogLibraryApiService: GogLibraryApiService,
    private val gogAuthTokenRepository: GogAuthTokenRepository,
    private val gameRepository: GameRepository
) {

    suspend fun getUserGames(): List<LibraryItem> {
        if (gogAuthTokenRepository.getAccessToken() == null) {
            return listOf()
        }

        var page = 0
        var gogProducts = listOf<GogProduct>()
        do {
            page++
            val response = gogLibraryApiService.getUserGames(page = page)
            gogProducts += response.products
        } while (page < response.totalPages)

        val gamesIds = gogProducts.map { it.id }

        if (gamesIds.isEmpty()) {
            return listOf()
        }

        val dbGames = gamesIds.chunked(IDS_CHUNK_SIZE).flatMap { idsChunk ->
            gameRepository.getGames(gogIds = idsChunk)
        }
        val dbGamesByGogId = dbGames.associateBy { it.ids.gogId }

        return gogProducts
            .map {
                val dbGame = dbGamesByGogId[it.id]

                LibraryItem(
                    igdbId = dbGame?.ids?.igdbId,
                    externalId = it.id,
                    name = dbGame?.name ?: it.title,
                    platform = LibraryItem.Platform.GOG,
                    url = dbGame?.url,
                    coverUrl = dbGame?.coverUrl,
                    thumbnailUrl = dbGame?.thumbnailUrl
                )
            }
    }

}