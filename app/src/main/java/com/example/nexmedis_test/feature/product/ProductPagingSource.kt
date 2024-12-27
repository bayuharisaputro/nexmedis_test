package com.example.nexmedis_test.feature.product

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.nexmedis_test.database.dao.ProductDao
import com.example.nexmedis_test.database.queryModel.ProductWithFavouriteEntity
import com.example.nexmedis_test.feature.product.datastore.ProductLocalDataStore

class ProductPagingSource(
    private val localDataStore: ProductLocalDataStore,
) : PagingSource<Int, ProductWithFavouriteEntity>() {
    init {
        Log.e("PagerDebug2", "PagingSource initialized")
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductWithFavouriteEntity> {
        val page = params.key ?: 0 // Start from page 0 if no key is provided
        val pageSize = params.loadSize // The size of each page of data
        val offset = page * pageSize // Calculate the offset based on page number and page size

        Log.d("PagingSource", "Loading page: $page with params: $params")
        Log.d("PagingSource", "Page size: $pageSize, Offset: $offset")

        return try {
            // Fetch data from the database
            val data = localDataStore.getAllProductsLocal()


            // Calculate nextKey
            val nextKey = 1

            // Calculate prevKey
            val prevKey = if (page == 0) null else page - 1

            // Log prevKey and nextKey for debugging
            Log.d("PagingSource", "PrevKey: $prevKey, NextKey: $nextKey")

            // Return the data along with the keys
            LoadResult.Page(
                data = emptyList(),
                prevKey = prevKey,
                nextKey = nextKey
            )

        } catch (e: Exception) {
            // Handle error
            Log.e("PagingSource", "Error loading data: ${e.message}")
            return LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, ProductWithFavouriteEntity>): Int? {
        Log.d("PagingSource", "PagingState: $state")
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}