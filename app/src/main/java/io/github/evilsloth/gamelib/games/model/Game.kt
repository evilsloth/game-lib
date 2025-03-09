package io.github.evilsloth.gamelib.games.model

data class Game(
    val ids: GameIds,
    val name: String,
    val url: String?,
    val rating: Double?,
    val userRating: Double?,
    val thumbnailUrl: String?,
    val coverUrl: String?
)
