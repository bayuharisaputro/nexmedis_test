package com.example.nexmedis_test.feature.product.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.nexmedis_test.apiService.NetworkUtils
import com.example.nexmedis_test.database.queryModel.ProductWithFavouriteEntity
import com.example.nexmedis_test.database.table.ProductEntity
import com.example.nexmedis_test.database.table.ProductFavouriteEntity
import com.example.nexmedis_test.feature.product.datastore.ProductLocalDataStore
import com.example.nexmedis_test.feature.product.datastore.ProductRemoteDataStore
import com.example.nexmedis_test.handler.ResponseState
import com.example.nexmedis_test.model.dto.ProductDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepoImpl @Inject constructor(
    private val localDataStore: ProductLocalDataStore,
    private val remoteDataStore: ProductRemoteDataStore,
    private val networkUtils: NetworkUtils
) : ProductRepo {

    override suspend fun getAllProductsLocal(query: String): Pager<Int, ProductWithFavouriteEntity> =
        withContext(Dispatchers.IO) {
            try {
                val pager = Pager(
                    config = PagingConfig(
                        pageSize = 5,
                        enablePlaceholders = false,
                        initialLoadSize = 10
                    ),
                    pagingSourceFactory = { localDataStore.getAllProductsLocal(query) }
                )

                return@withContext pager
            } catch (e: Exception) {
                throw e
            }
        }

    override suspend fun downloadProductFromServer(): ResponseState<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                if (networkUtils.isNetworkAvailable()) {
                    val result = remoteDataStore.getAllProducts()

                    if (result is ResponseState.Success) {
                        val productEntities =
                            result.data.map { mapProductDTOToProductEntity(it) }
                        localDataStore.saveAllProduct(productEntities)
                    } else {
                        throw (result as ResponseState.Error).exception
                    }
                }
                return@withContext ResponseState.Success(true)
            } catch (e: Exception) {
                return@withContext ResponseState.Error(e)
            }
        }


    override suspend fun getAllProductsFavourite(query: String):  Pager<Int, ProductWithFavouriteEntity> =
        withContext(Dispatchers.IO) {
            try {
                val pager = Pager(
                    config = PagingConfig(
                        pageSize = 5,
                        enablePlaceholders = false,
                        initialLoadSize = 10,
                    ),
                    pagingSourceFactory = { localDataStore.getAllProductsFavourite(query) }
                )

                return@withContext pager
            } catch (e: Exception) {
                throw e
            }
        }


    override suspend fun toggleFavoriteProduct(productId: Int): ResponseState<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                return@withContext ResponseState.Success(localDataStore.toggleFavoriteProduct(productId))
            } catch (e: Exception) {
                return@withContext ResponseState.Error(e)
            }
        }

    private fun mapProductDTOToProductEntity(productDto: ProductDTO): ProductEntity {
        return ProductEntity(
            id = productDto.id,
            title = productDto.title,
            price = productDto.price,
            description = productDto.description,
            category = productDto.category,
            image = productDto.image,
            rate = productDto.rating.rate,
            count = productDto.rating.count
        )
    }
}


