package io.github.evilsloth.gamelib.games

import io.github.evilsloth.gamelib.config.api.igdb.IgdbApiConstants
import io.github.evilsloth.gamelib.games.gamedb.GameDbApiService
import io.github.evilsloth.gamelib.games.model.Game
import io.github.evilsloth.gamelib.games.model.GameIds
import javax.inject.Inject

class GameRepository @Inject constructor(private val gameDbApiService: GameDbApiService) {

    suspend fun getGames(
        igdbIds: List<String>? = null,
        steamIds: List<String>? = null,
        gogIds: List<String>? = null,
        epicIds: List<String>? = null,
        amazonIds: List<String>? = null
    ): List<Game> {
        return gameDbApiService.getGames(
            ids = toIdsQuery(igdbIds),
            steamIds = toIdsQuery(steamIds),
            gogIds = toIdsQuery(gogIds),
            epicIds = toIdsQuery(epicIds),
            amazonIds = toIdsQuery(amazonIds),
        ).map {
            Game(
                ids = GameIds(
                    igdbId = it.id,
                    steamId = it.steamId,
                    gogId = it.gogId,
                    epicId = it.epicId,
                    epicSlug = it.epicSlug,
                    amazonId = it.amazonId
                ),
                name = it.name,
                url = "https://www.igdb.com/games/" + it.slug,
                rating = it.rating,
                userRating = it.userRating,
                thumbnailUrl = it.imageId?.let { imageId -> IgdbApiConstants.THUMBNAIL_URL + imageId + ".jpg" },
                coverUrl = it.imageId?.let { imageId -> IgdbApiConstants.COVERS_URL + imageId + ".jpg" }
            )
        }
    }

    private fun toIdsQuery(ids: List<String>?): String? {
        if (ids.isNullOrEmpty()) {
            return null
        }

        return ids.joinToString(",")
    }

}