package com.example.nexmedis_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nexmedis_test.feature.product.viewModel.ProductViewModelImpl
import com.example.nexmedis_test.ui.mainCompose.NavChild
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val viewModel: ProductViewModelImpl = hiltViewModel()
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "all_products",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("all_products") {
                NavChild().AllProductsScreen(viewModel)
            }
            composable("favorite_products") { NavChild().FavoriteProductsScreen(viewModel) }
        }
    }
}


@Composable
fun NavigationBar(navController: NavController, ) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route
        ?: "all_products"

    val selectedRoute = remember { mutableStateOf(currentRoute) }

    NavigationBar {
        NavigationBarItem(
            selected = selectedRoute.value == "all_products",
            onClick = {
                if (selectedRoute.value != "all_products") {
                    navController.navigate("all_products") {
                        popUpTo("all_products") { inclusive = true }
                    }
                    selectedRoute.value = "all_products"
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "All Products"
                )
            },
            label = {
                Text("All Products")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Blue,
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color.Blue,
                unselectedTextColor = Color.Gray
            )
        )

        NavigationBarItem(
            selected = selectedRoute.value == "favorite_products",
            onClick = {
                if (selectedRoute.value != "favorite_products") {
                    navController.navigate("favorite_products") {
                        popUpTo("favorite_products") { inclusive = true }
                    }
                    selectedRoute.value = "favorite_products"
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite Products"
                )
            },
            label = {
                Text("Favorite Products")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Red,
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color.Red,
                unselectedTextColor = Color.Gray
            )
        )
    }
}



