package io.github.evilsloth.gamelib.library.egs.products

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EgsProductDao {
    @Query("SELECT * FROM egs_products WHERE id = :id")
    fun getById(id: String): EgsProductEntity?

    @Insert
    fun insert(product: EgsProductEntity)
}