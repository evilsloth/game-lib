package io.github.evilsloth.gamelib.library.egs.products

data class EgsProductDetailsResponse(
    val id: String,
    val title: String,
    val shortDescription: String,
    val mapping: EgsProductMapping
)
