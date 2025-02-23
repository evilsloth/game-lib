package io.github.evilsloth.gamelib.library.egs.products

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "EGS_PRODUCTS")
data class EgsProductEntity(
    @PrimaryKey val id: String,
    @ColumnInfo val title: String,
    @ColumnInfo val slug: String
)