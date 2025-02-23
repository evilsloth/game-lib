package io.github.evilsloth.gamelib.library.steam

data class SteamUserGamesResponseData(
    val game_count: Int,
    val games: List<SteamUserGame>
)
