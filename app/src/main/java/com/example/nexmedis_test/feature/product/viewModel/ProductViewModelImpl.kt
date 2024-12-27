package com.example.nexmedis_test.feature.product.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.nexmedis_test.database.queryModel.ProductWithFavouriteEntity
import com.example.nexmedis_test.database.table.ProductEntity
import com.example.nexmedis_test.feature.product.repository.ProductRepo
import com.example.nexmedis_test.handler.BaseLiveDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModelImpl @Inject constructor(
    private val productRepository: ProductRepo
) : ViewModel(), ProductViewModel {

    private val mLiveDataProductList =
        MutableLiveData<BaseLiveDataState<Flow<PagingData<ProductWithFavouriteEntity>>>>()

    private val mLiveDataToggleFavorite = MutableLiveData<BaseLiveDataState<ProductWithFavouriteEntity>>()

    override val liveDataProductList: LiveData<BaseLiveDataState<Flow<PagingData<ProductWithFavouriteEntity>>>> =
        mLiveDataProductList

    override val liveDataToggleFavorite: LiveData<BaseLiveDataState<ProductWithFavouriteEntity>> =
        mLiveDataToggleFavorite

    init {
        viewModelScope.launch {
            productRepository.downloadProductFromServer()
        }
    }



    override fun getProductList(isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                mLiveDataProductList.value = BaseLiveDataState(isLoading = true)
                val response = if(isFavorite) {
                    productRepository.getAllProductsFavourite().flow
                        .cachedIn(viewModelScope)
                } else {
                    productRepository.getAllProductsLocal().flow
                        .cachedIn(viewModelScope)
                }
                mLiveDataProductList.value = BaseLiveDataState(
                    isLoading = false,
                    data = response,
                    errorException = null
                )

            } catch (e: Exception) {
                mLiveDataProductList.value = BaseLiveDataState(
                    isLoading = false,
                    data = null,
                    errorException = e
                )
            }
        }
    }

    override fun updateFavoriteProduct(product: ProductWithFavouriteEntity) {
        viewModelScope.launch {
            try {
                mLiveDataToggleFavorite.value = BaseLiveDataState(isLoading = true)
                productRepository.toggleFavoriteProduct(product.id)
                mLiveDataToggleFavorite.value = BaseLiveDataState(
                    isLoading = false,
                    data = product.copy(isFavorite = !product.isFavorite),
                    errorException = null
                )

            } catch (e: Exception) {
                mLiveDataToggleFavorite.value = BaseLiveDataState(
                    isLoading = false,
                    data = null,
                    errorException = e
                )
            }
        }
    }
}