package io.github.evilsloth.gamelib.library.gog

data class GogUserGamesResponse(
    val page: Int,
    val totalProducts: Int,
    val totalPages: Int,
    val products: List<GogProduct>
)