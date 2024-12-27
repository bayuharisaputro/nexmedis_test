package com.example.nexmedis_test.dependencyInjection.repositoryModule

import com.example.nexmedis_test.apiService.NetworkUtils
import com.example.nexmedis_test.feature.product.datastore.ProductLocalDataStore
import com.example.nexmedis_test.feature.product.datastore.ProductRemoteDataStore
import com.example.nexmedis_test.feature.product.repository.ProductRepo
import com.example.nexmedis_test.feature.product.repository.ProductRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ProductRepositoryModule {

    @Provides
    fun provideProductRepository(
        localDatastore: ProductLocalDataStore,
        remoteDatastore: ProductRemoteDataStore,
        networkUtils: NetworkUtils,
    ): ProductRepo {
        return ProductRepoImpl(
            localDatastore,
            remoteDatastore,
            networkUtils
        )

    }

}