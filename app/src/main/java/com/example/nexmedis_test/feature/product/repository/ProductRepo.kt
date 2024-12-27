package com.example.nexmedis_test.feature.product.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.example.nexmedis_test.database.queryModel.ProductWithFavouriteEntity
import com.example.nexmedis_test.database.table.ProductEntity
import com.example.nexmedis_test.handler.ResponseState
import kotlinx.coroutines.flow.Flow

interface ProductRepo {
    suspend fun getAllProductsLocal(query: String = ""): Pager<Int, ProductWithFavouriteEntity>
    suspend fun downloadProductFromServer(): ResponseState<Boolean>
    suspend fun getAllProductsFavourite(query: String = ""): Pager<Int, ProductWithFavouriteEntity>
    suspend fun toggleFavoriteProduct(productId :Int): ResponseState<Boolean>
}