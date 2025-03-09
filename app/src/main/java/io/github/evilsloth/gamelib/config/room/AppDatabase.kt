package io.github.evilsloth.gamelib.config.room

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.evilsloth.gamelib.library.egs.products.EgsProductDao
import io.github.evilsloth.gamelib.library.egs.products.EgsProductEntity
import io.github.evilsloth.gamelib.library.model.LibraryItem
import io.github.evilsloth.gamelib.library.model.LibraryItemDao

@Database(
    entities = [EgsProductEntity::class, LibraryItem::class],
    version = 6
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun egsProductDao(): EgsProductDao
    abstract fun libraryItemDao(): LibraryItemDao
}