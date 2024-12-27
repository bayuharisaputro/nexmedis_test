package com.example.nexmedis_test.feature.product.datastore

import android.util.Log
import androidx.paging.PagingSource
import com.example.nexmedis_test.database.dao.ProductDao
import com.example.nexmedis_test.database.queryModel.ProductWithFavouriteEntity
import com.example.nexmedis_test.database.table.ProductEntity
import com.example.nexmedis_test.database.table.ProductFavouriteEntity
import com.example.nexmedis_test.handler.ResponseState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductLocalDataStoreImpl @Inject constructor(
    private val productDAO: ProductDao,
) : ProductLocalDataStore {

    override fun getAllProductsLocal(): PagingSource<Int, ProductWithFavouriteEntity> {
        return try {
            productDAO.getAllProductWithFavouriteModifier()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun isProductExist(): Boolean {
        return try {
            productDAO.getProductCount() > 0
        } catch (e: Exception) {
            false
        }
    }

    override fun getAllProductsFavourite(): PagingSource<Int, ProductWithFavouriteEntity> {
        return try {
            productDAO.getAllProductFavourite()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun saveAllProduct(products: List<ProductEntity>?): Boolean {
        return try {
            productDAO.deleteAllProducts()
            productDAO.insertProducts(products ?: emptyList())
            true
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun toggleFavoriteProduct(productId: Int): Boolean {
        return try {
            if(productDAO.getProductFavoriteById(productId) == null) {
                productDAO.insertFavouriteProduct(ProductFavouriteEntity(productId))
            } else {
                productDAO.removeProductFromFavorites(productId)
            }

            true
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteFavouriteProduct(productId: Int): Boolean {
        return try {
            productDAO.removeProductFromFavorites(productId)
            true
        } catch (e: Exception) {
            throw e
        }
    }
}
