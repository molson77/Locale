package com.example.locale.data.model

import com.squareup.moshi.Json

data class Location(
    val address1: String? = null,
    val address2: String? = null,
    val address3: String? = null,
    val city: String? = null,
    @Json(name = "zip_code")
    val zipCode: String? = null,
    val country: String? = null,
    val state: String? = null,
    @Json(name = "display_address")
    val displayAddress: List<String>,
    @Json(name = "cross_streets")
    val crossStreets: String? = null,
)
