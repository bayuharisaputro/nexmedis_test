package com.example.nexmedis_test.ui.mainCompose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.nexmedis_test.feature.product.viewModel.ProductViewModelImpl
import com.example.nexmedis_test.ui.productCompose.ProductView
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

class NavChild {
    @Composable
    fun AllProductsScreen(viewModel: ProductViewModelImpl) {

        val productState by viewModel.liveDataProductList.observeAsState()
        val pagingData = remember(productState?.data) { productState?.data }

        var isRefreshing by remember { mutableStateOf(false) }
        var showErrorDialog by remember { mutableStateOf(false) }
        var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

        val isLoading by viewModel.liveDataLoadingServerSync.observeAsState()
        var isDataLoaded by remember { mutableStateOf(false) }

        LaunchedEffect(isLoading) {
            if (isLoading?.data == false) {
                isDataLoaded = true
            }
        }

        val onRefresh = {
            isRefreshing = true
            viewModel.getProductList(isFavorite = false, queryTitle = searchQuery.text)
            isRefreshing = false
        }

        LaunchedEffect(searchQuery, isDataLoaded) {
            if (!isDataLoaded) viewModel.getProductList(
                isFavorite = false,
                queryTitle = searchQuery.text
            )
        }

        if (isLoading?.isLoading == true) {
            LoadingIndicator()
        }

        Box(modifier = Modifier.fillMaxSize()) {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = onRefresh
            ) {
                Column {
                    TextField(
                        value = searchQuery,
                        onValueChange = { newText ->
                            searchQuery = newText
                        },
                        label = { Text("Search Products By Title") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    )

                    if (pagingData != null) {
                        ProductView().ProductList(pagingData = pagingData, viewModel)
                    }

                    if (productState?.isLoading == true) {
                        LoadingIndicator()
                    }

                    if (productState?.errorException != null) {
                        showErrorDialog = true
                    }

                    if (showErrorDialog) {
                        ErrorDialog(
                            errorMessage = productState?.errorException?.message
                                ?: "An error occurred",
                            onDismiss = {
                                showErrorDialog = false
                            }
                        )
                    }
                }
            }


        }
    }


    @Composable
    fun FavoriteProductsScreen(viewModel: ProductViewModelImpl) {
        val productState by viewModel.liveDataProductList.observeAsState()
        val pagingData = remember(productState?.data) { productState?.data }

        var isRefreshing by remember { mutableStateOf(false) }
        var showErrorDialog by remember { mutableStateOf(false) }
        var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

        val onRefresh = {
            isRefreshing = true
            viewModel.getProductList(isFavorite = true, queryTitle = searchQuery.text)
            isRefreshing = false
        }

        LaunchedEffect(searchQuery) {
            viewModel.getProductList(isFavorite = true, queryTitle = searchQuery.text)
        }

        Box(modifier = Modifier.fillMaxSize()) {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = onRefresh
            ) {
                Column {
                    TextField(
                        value = searchQuery,
                        onValueChange = { newText ->
                            searchQuery = newText
                        },
                        label = { Text("Search Products By Title") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    )

                    if (pagingData != null) {
                        ProductView().ProductList(pagingData = pagingData, viewModel)
                    }

                    if (productState?.isLoading == true) {
                        LoadingIndicator()
                    }

                    if (productState?.errorException != null) {
                        showErrorDialog = true
                    }

                    if (showErrorDialog) {
                        ErrorDialog(
                            errorMessage = productState?.errorException?.message
                                ?: "An error occurred",
                            onDismiss = {
                                showErrorDialog = false
                            }
                        )
                    }
                }
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
    fun ErrorDialog(
        errorMessage: String?,
        onDismiss: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Error", style = MaterialTheme.typography.headlineMedium)
            },
            text = {
                Text(
                    text = errorMessage ?: "An error occurred",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(onClick = { onDismiss.invoke() }) {
                    Text("Close")
                }
            },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

    }

}