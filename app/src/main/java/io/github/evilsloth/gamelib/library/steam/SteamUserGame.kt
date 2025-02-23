package io.github.evilsloth.gamelib.library.steam

data class SteamUserGame(
    val appid: Long,
    val name: String,
    val playtime_forever: Int
)
