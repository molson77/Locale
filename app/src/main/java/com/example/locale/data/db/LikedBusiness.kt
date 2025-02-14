package com.example.locale.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liked_business")
data class LikedBusiness(
    @PrimaryKey val id: String
)