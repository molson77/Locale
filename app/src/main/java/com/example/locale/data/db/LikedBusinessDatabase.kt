package com.example.locale.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LikedBusiness::class], version = 1)
abstract class LikedBusinessDatabase : RoomDatabase() {
    abstract fun likedBusinessDao(): LikedBusinessDao
}