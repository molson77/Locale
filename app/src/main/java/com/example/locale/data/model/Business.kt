package com.example.locale.data.model

data class Business(
    val id: String,
    val alias: String,
    val name: String,
    val imageUrl: String?,
    val isClosed: Boolean?,
    val url: String?,
    val reviewCount: String?,
    val categories: List<Category>?,
    val rating: String?,
    val coordinates: Coordinates?,
    val transactions: List<String>?,
    val price: String?,
    val location: Location?,
    val phone: String?,
    val displayPhone: String?,
    val distance: String?,
    val businessHours: BusinessHours?,
    val liked: Boolean = false
)
