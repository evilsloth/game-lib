package io.github.evilsloth.gamelib.library.steam

import retrofit2.http.GET
import retrofit2.http.Query

interface SteamLibraryApiService {

    @GET("IPlayerService/GetOwnedGames/v0001")
    suspend fun getUserGames(
        @Query("key") apiKey: String,
        @Query("steamid") steamId: String,
        @Query("include_appinfo") includeAppinfo: Boolean = true,
        @Query("include_played_free_games") includePlayedFreeGames: Boolean = true,
        @Query("format") format: String = "json"
    ): SteamUserGamesResponse

}
