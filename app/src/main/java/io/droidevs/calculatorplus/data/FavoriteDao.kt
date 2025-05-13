package io.droidevs.calculatorplus.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity): Long

    @Transaction
    @Query("SELECT * FROM favorites ORDER BY favId DESC")
    suspend fun getFavoritesWithHistory(): List<FavoriteWithHistory>

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)
}
