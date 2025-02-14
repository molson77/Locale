package com.example.locale.data.model

import com.squareup.moshi.Json
data class BusinessDetails(
    val id: String,
    val alias: String,
    val name: String,
    @Json(name = "image_url")
    val imageUrl: String?,
    @Json(name = "is_closed")
    val isClosed: Boolean?,
    val url: String?,
    @Json(name = "review_count")
    val reviewCount: Int?,
    val categories: List<Category>?,
    val rating: Double?,
    val coordinates: Coordinates?,
    val transactions: List<String>?,
    val price: String?,
    val location: Location?,
    val phone: String?,
    @Json(name = "display_phone")
    val displayPhone: String?,
    val distance: String?,
    @Json(name = "is_claimed")
    val isClaimed: Boolean,
    @Json(name = "date_opened")
    val dateOpened: String?,
    @Json(name = "date_closed")
    val dateClosed: String?,
    val photos: List<String>,
    val hours: List<BusinessHours>?,
    val attributes: Attributes?,
    val liked: Boolean = false
)


data class Attributes(
    val menuUrl: String?
)
