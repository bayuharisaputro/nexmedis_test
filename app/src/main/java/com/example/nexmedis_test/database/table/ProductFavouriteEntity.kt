package com.example.nexmedis_test.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productFavourites")
data class ProductFavouriteEntity(
    @PrimaryKey val productId: Int
)