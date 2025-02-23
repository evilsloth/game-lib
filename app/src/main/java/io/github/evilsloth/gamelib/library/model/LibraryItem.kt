package io.github.evilsloth.gamelib.library.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LIBRARY_ITEMS")
data class LibraryItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "igdb_id") val igdbId: Long?,
    @ColumnInfo(name = "external_id") val externalId: String,
    val name: String,
    val platform: Platform,
    val url: String?,
    @ColumnInfo(name = "cover_url") val coverUrl: String?,
    @ColumnInfo(name = "thumbnail_url") val thumbnailUrl: String?
) {
    enum class Platform {
        AMAZON, EGS, GOG, STEAM
    }
}