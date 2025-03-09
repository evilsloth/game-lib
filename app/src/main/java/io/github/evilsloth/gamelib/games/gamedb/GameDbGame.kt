package io.github.evilsloth.gamelib.games.gamedb

data class GameDbGame(
    val id: Long,
    val slug: String,
    val name: String,
    val imageId: String?,
    val steamId: String?,
    val gogId: String?,
    val epicId: String?,
    val epicSlug: String?,
    val amazonId: String?,
    val rating: Double?,
    val userRating: Double?
)