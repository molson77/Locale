package com.example.locale.data.model

data class BusinessSearchResponse(
    val businesses: List<Business>,
    val total: Int
)