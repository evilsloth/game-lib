package io.github.evilsloth.gamelib.library.igdb;

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

private const val PAGE_SIZE = 50
private const val FIELDS = "id, name, url, cover.id, cover.image_id, cover.url, " +
        "external_games.id, external_games.category, external_games.uid, external_games.url, " +
        "websites.id, websites.category, websites.url"

class IgdbGamesRepository @Inject constructor(
    private val igdbGamesApiService: IgdbGamesApiService
) {

    suspend fun getGames(whereQuery: String): List<IgdbGame> {
        val query = "fields $FIELDS; where $whereQuery; limit $PAGE_SIZE; sort name;"
        var results: List<IgdbGame>? = null
        var offset = 0
        do {
            val offsetQuery = "$query offset $offset;"
            val games = igdbGamesApiService.getGames(offsetQuery.toRequestBody("text/plain".toMediaType()))
            results = if (results == null) games else results + games
            offset += PAGE_SIZE
        } while (games.size >= PAGE_SIZE)

        return results!!
    }

}
