package com.example.nexmedis_test.feature.product.viewModel

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.nexmedis_test.database.queryModel.ProductWithFavouriteEntity
import com.example.nexmedis_test.handler.BaseLiveDataState
import kotlinx.coroutines.flow.Flow

interface ProductViewModel {

    val liveDataProductList: LiveData<BaseLiveDataState<Flow<PagingData<ProductWithFavouriteEntity>>>>

    val liveDataToggleFavorite: LiveData<BaseLiveDataState<ProductWithFavouriteEntity>>

    val liveDataLoadingServerSync: LiveData<BaseLiveDataState<Boolean>>

    fun getProductList(isFavorite: Boolean = false,  queryTitle: String = "")
    fun updateFavoriteProduct(product: ProductWithFavouriteEntity)
}