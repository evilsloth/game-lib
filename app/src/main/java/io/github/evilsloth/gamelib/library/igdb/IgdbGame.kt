package io.github.evilsloth.gamelib.library.igdb

data class IgdbGame(
    val id: Long,
    val name: String,
    val url: String,
    val cover: IgdbGameCover,
    val external_games: List<IgdbExternalGame>?,
    val websites: List<IgdbWebsite>?,
)