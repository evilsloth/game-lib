package io.github.evilsloth.gamelib.library.igdb;

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST;

interface IgdbGamesApiService {

    @POST("games")
    suspend fun getGames(@Body body: RequestBody): List<IgdbGame>

}
