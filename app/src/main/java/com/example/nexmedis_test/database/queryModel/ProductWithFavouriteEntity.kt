package com.example.nexmedis_test.database.queryModel

data class ProductWithFavouriteEntity(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rate: Double,
    val count: Int,
    var isFavorite: Boolean
)