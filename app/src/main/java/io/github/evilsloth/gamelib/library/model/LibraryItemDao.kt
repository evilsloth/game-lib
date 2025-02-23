package io.github.evilsloth.gamelib.library.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryItemDao {

    @Query("SELECT * FROM library_items")
    fun getAll(): Flow<List<LibraryItem>>

    @Query("SELECT * FROM library_items WHERE name LIKE '%' || :name || '%'")
    fun getAllByNameLike(name: String): Flow<List<LibraryItem>>

    @Query("DELETE FROM library_items")
    fun deleteAll()

    @Insert
    fun insertAll(items: List<LibraryItem>)

}