package io.github.evilsloth.gamelib.games.gamedb

import retrofit2.http.GET
import retrofit2.http.Query

interface GameDbApiService {

    @GET("games")
    suspend fun getGames(
        @Query("id") ids: String? = null,
        @Query("steamId") steamIds: String? = null,
        @Query("gogId") gogIds: String? = null,
        @Query("epicId") epicIds: String? = null,
        @Query("amazonId") amazonIds: String? = null
    ): List<GameDbGame>

}