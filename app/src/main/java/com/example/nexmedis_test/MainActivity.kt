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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                NavChild().AllProductsScreen(viewModel )
            }
            composable("favorite_products") { NavChild().FavoriteProductsScreen(viewModel) }
        }
    }
}

@Composable
fun NavigationBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            selected = navController.currentBackStackEntry?.destination?.route == "all_products",
            onClick = { navController.navigate("all_products") },
            label = { Text("All Products") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) }
        )
        NavigationBarItem(
            selected = navController.currentBackStackEntry?.destination?.route == "favorite_products",
            onClick = { navController.navigate("favorite_products") },
            label = { Text("Favorite Products") },
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) }
        )
    }
}

