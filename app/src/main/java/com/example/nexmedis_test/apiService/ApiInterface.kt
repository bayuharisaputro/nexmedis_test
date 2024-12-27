package com.example.nexmedis_test.apiService

import com.example.nexmedis_test.model.dto.ProductDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("products?limit=20")
    suspend fun getAllProducts(): Response<List<ProductDTO>>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ProductDTO>
}