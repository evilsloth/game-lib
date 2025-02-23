package io.github.evilsloth.gamelib.library.egs.library

data class EgsUserLibraryResponse(
    val responseMetadata: EgsResponseMetadata?,
    val records: List<EgsUserLibraryItem>
)
