package com.example.nexmedis_test.feature.product.datastore

import android.util.Log
import com.example.nexmedis_test.apiService.ApiInterface
import com.example.nexmedis_test.handler.ResponseState
import com.example.nexmedis_test.model.dto.ProductDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRemoteDataStoreImpl @Inject constructor(
    private val apiService: ApiInterface
) : ProductRemoteDataStore {
    override suspend fun getAllProducts(): ResponseState<List<ProductDTO>> {
        return try {
            val response = apiService.getAllProducts()

            if (response.isSuccessful) {
                val products = response.body() ?: emptyList()
                ResponseState.Success(products)
            } else {
                ResponseState.Error(Throwable("Error: ${response.message()}"))
            }
        } catch (exception: Exception) {
            ResponseState.Error(exception)
        }
    }
}
