package io.github.evilsloth.gamelib.library.egs.library

data class EgsUserLibraryItem(
    val namespace: String,
    val catalogItemId: String,
    val appName: String,
    val productId: String,
    val sandboxName: String,
    val sandboxType: String,
    val recordType: String,
    val acquisitionDate: String,
    val availableDate: String
)
