package com.example.nexmedis_test.feature.product.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.nexmedis_test.database.queryModel.ProductWithFavouriteEntity
import com.example.nexmedis_test.feature.product.repository.ProductRepo
import com.example.nexmedis_test.handler.BaseLiveDataState
import com.example.nexmedis_test.handler.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModelImpl @Inject constructor(
    private val productRepository: ProductRepo
) : ViewModel(), ProductViewModel {

    private val mLiveDataProductList =
        MutableLiveData<BaseLiveDataState<Flow<PagingData<ProductWithFavouriteEntity>>>>()

    private val mLiveDataToggleFavorite = MutableLiveData<BaseLiveDataState<ProductWithFavouriteEntity>>()

    private val mLiveDataLoadingServerSync= MutableLiveData<BaseLiveDataState<Boolean>>()

    override val liveDataProductList: LiveData<BaseLiveDataState<Flow<PagingData<ProductWithFavouriteEntity>>>> =
        mLiveDataProductList

    override val liveDataToggleFavorite: LiveData<BaseLiveDataState<ProductWithFavouriteEntity>> =
        mLiveDataToggleFavorite

    override val liveDataLoadingServerSync: LiveData<BaseLiveDataState<Boolean>> =
        this.mLiveDataLoadingServerSync

    init {
        viewModelScope.launch {
            try {
                mLiveDataLoadingServerSync.value = BaseLiveDataState(isLoading = true)
                val request = productRepository.downloadProductFromServer()
                if(request is ResponseState.Error) {
                    throw request.exception
                }
                mLiveDataLoadingServerSync.value = BaseLiveDataState(
                    isLoading = false,
                    errorException = null
                )
            } catch (e: Exception) {
                mLiveDataLoadingServerSync.value = BaseLiveDataState(
                    isLoading = false,
                    errorException = e
                )
            }

        }
    }


    override fun getProductList(isFavorite: Boolean, queryTitle: String) {
        viewModelScope.launch {
            try {
                mLiveDataProductList.value = BaseLiveDataState(isLoading = true)
                val response = if(isFavorite) {
                    productRepository.getAllProductsFavourite(queryTitle).flow
                        .cachedIn(viewModelScope)
                } else {
                    productRepository.getAllProductsLocal(queryTitle).flow
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