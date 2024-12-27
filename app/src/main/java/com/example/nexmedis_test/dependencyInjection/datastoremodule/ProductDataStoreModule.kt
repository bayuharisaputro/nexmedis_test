package com.example.nexmedis_test.dependencyInjection.datastoremodule

import com.example.nexmedis_test.apiService.ApiInterface
import com.example.nexmedis_test.database.dao.ProductDao
import com.example.nexmedis_test.feature.product.datastore.ProductLocalDataStore
import com.example.nexmedis_test.feature.product.datastore.ProductLocalDataStoreImpl
import com.example.nexmedis_test.feature.product.datastore.ProductRemoteDataStore
import com.example.nexmedis_test.feature.product.datastore.ProductRemoteDataStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ProductDataStoreModule {

    @Provides
    fun provideProductRemoteDataStore(
        apiService: ApiInterface
    ): ProductRemoteDataStore {
        return ProductRemoteDataStoreImpl(
            apiService = apiService
        )
    }
    @Provides
    fun provideProductLocalDataStore(
        productDao: ProductDao
    ): ProductLocalDataStore {
        return ProductLocalDataStoreImpl(
            productDAO = productDao
        )
    }
}