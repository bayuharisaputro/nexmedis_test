package com.example.nexmedis_test.feature.product.datastore

import androidx.paging.PagingSource
import com.example.nexmedis_test.database.queryModel.ProductWithFavouriteEntity
import com.example.nexmedis_test.database.table.ProductEntity
import com.example.nexmedis_test.handler.ResponseState

interface ProductLocalDataStore {
    fun getAllProductsLocal(): PagingSource<Int, ProductWithFavouriteEntity>
    suspend fun isProductExist(): Boolean
    fun getAllProductsFavourite():PagingSource<Int, ProductWithFavouriteEntity>
    suspend fun saveAllProduct(products: List<ProductEntity>?): Boolean
    suspend fun toggleFavoriteProduct(productId :Int): Boolean
    suspend fun deleteFavouriteProduct(productId :Int): Boolean
}