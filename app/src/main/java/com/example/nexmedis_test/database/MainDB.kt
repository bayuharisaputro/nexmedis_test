package com.example.nexmedis_test.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nexmedis_test.database.dao.ProductDao
import com.example.nexmedis_test.database.table.ProductEntity
import com.example.nexmedis_test.database.table.ProductFavouriteEntity


@Database(entities = [ProductEntity::class, ProductFavouriteEntity::class], version = 1, exportSchema = false)
abstract class MainDB : RoomDatabase() {
    abstract fun productDAO(): ProductDao
}
