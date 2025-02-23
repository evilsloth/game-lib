package io.github.evilsloth.gamelib.library.igdb

data class IgdbExternalGame(
    val id: Long,
    val category: Int,
    val uid: String,
    val url: String?
)
