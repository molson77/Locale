package com.example.locale.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LikedBusinessDao {
    @Query("SELECT * FROM liked_business")
    suspend fun getAll(): List<LikedBusiness>

    @Query("SELECT * FROM liked_business WHERE id=:id LIMIT 1")
    suspend fun findBusiness(id: String): LikedBusiness?

    @Insert
    suspend fun insert(likedBusiness: LikedBusiness)

    @Delete
    suspend fun delete(likedBusiness: LikedBusiness)
}