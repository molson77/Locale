package com.example.locale.data.model

data class Location(
    val address1: String? = null,
    val address2: String? = null,
    val address3: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val country: String? = null,
    val state: String? = null,
    val displayAddress: List<String>,
    val crossStreets: String? = null,
)
