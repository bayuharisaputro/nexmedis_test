package com.example.nexmedis_test.model.dto

data class ProductDTO(
    val id: Int = -1,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: RatingDto
)

data class RatingDto(
    val rate: Double,
    val count: Int
)