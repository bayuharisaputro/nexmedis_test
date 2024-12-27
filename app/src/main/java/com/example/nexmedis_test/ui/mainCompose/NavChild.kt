package com.example.nexmedis_test.ui.mainCompose

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.nexmedis_test.feature.product.viewModel.ProductViewModelImpl
import com.example.nexmedis_test.ui.productCompose.ProductView

class NavChild {
    @Composable
    fun AllProductsScreen(viewModel: ProductViewModelImpl) {

        val productState by viewModel.liveDataProductList.observeAsState()
        val pagingData = remember(productState?.data) { productState?.data }

        LaunchedEffect(Unit) {
            viewModel.getProductList(isFavorite = false)
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (pagingData != null) {
                ProductView().ProductList(pagingData = pagingData, viewModel)
            }

            if (productState?.isLoading == true) {
                LoadingIndicator()
            }

            if (productState?.errorException != null) {
                ErrorMessage(
                    message = productState?.errorException?.message ?: "An error occurred",

                )
            }
        }
    }

    @Composable
    fun LoadingIndicator() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

    }

    @Composable
    fun ErrorMessage(message: String) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = message, color = Color.Red)
        }
    }


    @Composable
    fun FavoriteProductsScreen(viewModel: ProductViewModelImpl) {
        val productState by viewModel.liveDataProductList.observeAsState()
        val pagingData = remember(productState?.data) { productState?.data }

        LaunchedEffect(Unit) {
            viewModel.getProductList(true)
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (pagingData != null) {
                ProductView().ProductList(pagingData = pagingData, viewModel)
            }

            if (productState?.isLoading == true) {
                LoadingIndicator()
            }

            if (productState?.errorException != null) {
                ErrorMessage(
                    message = productState?.errorException?.message ?: "An error occurred",
                    )
            }
        }
    }
}