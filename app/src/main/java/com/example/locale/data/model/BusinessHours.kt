package com.example.locale.data.model

data class BusinessHours(
    val hoursType: String,
    val open: List<BusinessDay>,
    val isOpenNow: Boolean
)
