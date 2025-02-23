package io.github.evilsloth.gamelib.library.egs.library

import retrofit2.http.GET
import retrofit2.http.Query

interface EgsLibraryApiService {

    @GET("library/api/public/items")
    suspend fun getUserLibrary(
        @Query("includeMetadata") includeMetadata: Boolean = true,
        @Query("cursor") cursor: String? = null
    ): EgsUserLibraryResponse

}
