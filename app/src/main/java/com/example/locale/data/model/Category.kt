package com.example.locale.data.model

data class Category(
    val alias: String,
    val title: String
) {
    override fun toString(): String {
        return title
    }
}
