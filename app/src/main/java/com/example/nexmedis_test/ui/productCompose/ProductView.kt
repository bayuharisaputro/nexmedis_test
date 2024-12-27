package com.example.nexmedis_test.ui.productCompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.nexmedis_test.database.queryModel.ProductWithFavouriteEntity
import com.example.nexmedis_test.feature.product.viewModel.ProductViewModel
import kotlinx.coroutines.flow.Flow


class ProductView {
    @Composable
    fun ProductList(pagingData: Flow<PagingData<ProductWithFavouriteEntity>>?, viewModel: ProductViewModel) {
        val lazyPagingItems = pagingData?.collectAsLazyPagingItems()
        val listState = rememberLazyListState()

        if ((lazyPagingItems?.itemCount ?: 0) < 1){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No products available.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                )
            }
        }

        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues( 8.dp)
        ) {
            items(lazyPagingItems?.itemCount ?: 0, key = { index ->
                lazyPagingItems?.get(index)?.id ?: 0 // Use unique id as key
            }) { index ->
                val product = lazyPagingItems?.get(index)

                product?.let {
                    ProductItem (
                        product = it,
                        onFavoriteToggle = { updatedProduct ->
                            viewModel.updateFavoriteProduct(updatedProduct)
                        }
                    )
                }
            }

            lazyPagingItems?.apply {
                when (loadState.append) {
                    is LoadState.Loading -> {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }

                    is LoadState.Error -> {
                        item {
                            val error = loadState.append as LoadState.Error
                            Text(
                                text = "Error: ${error.error.message}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    @Composable
    fun ProductItem(product: ProductWithFavouriteEntity,
                    onFavoriteToggle: (ProductWithFavouriteEntity) -> Unit) {
        var isFavorite by remember { mutableStateOf(product.isFavorite) }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(),
            shape = RoundedCornerShape(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Image(
                    painter = rememberAsyncImagePainter(product.image), // Replace with actual image URL field
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxHeight()
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp)), // Optional for rounded corners
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$${product.price}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Blue
                    )
                }

                IconButton(
                    onClick = {
                        isFavorite = !isFavorite
                        onFavoriteToggle(product.copy(isFavorite = isFavorite))

                    },
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
            }
        }
    }
}