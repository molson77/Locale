package com.example.locale.data.model

data class BusinessDay(
    val day: Int,
    val start: Int,
    val end: Int,
    val isOvernight: Boolean
)
