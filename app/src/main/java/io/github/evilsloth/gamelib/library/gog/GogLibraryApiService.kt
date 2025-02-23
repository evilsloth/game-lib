package io.github.evilsloth.gamelib.library.gog

import retrofit2.http.GET
import retrofit2.http.Query

interface GogLibraryApiService {

    @GET("account/getFilteredProducts")
    suspend fun getUserGames(
        @Query("mediaType") mediaType: String = "1",
        @Query("page") page: Int
    ): GogUserGamesResponse

}
