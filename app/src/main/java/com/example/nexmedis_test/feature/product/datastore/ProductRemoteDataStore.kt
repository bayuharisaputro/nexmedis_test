package com.example.nexmedis_test.feature.product.datastore

import com.example.nexmedis_test.handler.ResponseState
import com.example.nexmedis_test.model.dto.ProductDTO

interface ProductRemoteDataStore {
    suspend fun getAllProducts(): ResponseState<List<ProductDTO>>
}