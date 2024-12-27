package com.example.nexmedis_test.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nexmedis_test.database.queryModel.ProductWithFavouriteEntity
import com.example.nexmedis_test.database.table.ProductEntity
import com.example.nexmedis_test.database.table.ProductFavouriteEntity

@Dao
interface ProductDao {

    @Query("SELECT COUNT(*) FROM products")
    fun getProductCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("DELETE from products where 1 = 1")
    fun deleteAllProducts()

    @Query("SELECT * FROM productFavourites WHERE productId = :id")
    suspend fun getProductFavoriteById(id: Int): ProductFavouriteEntity?

    @Query("""
        SELECT products.*, 
               CASE WHEN productFavourites.productId IS NOT NULL THEN 1 ELSE 0 END AS isFavorite
        FROM products
        LEFT JOIN productFavourites ON products.id = productFavourites.productId
        ORDER BY products.id ASC
    """)
    fun getAllProductWithFavouriteModifier(): PagingSource<Int, ProductWithFavouriteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteProduct(products: ProductFavouriteEntity)

    @Query("""
    SELECT products.*, 
           CASE WHEN productFavourites.productId IS NOT NULL THEN 1 ELSE 0 END AS isFavorite
    FROM products
    LEFT JOIN productFavourites ON products.id = productFavourites.productId
    WHERE productFavourites.productId IS NOT NULL
    ORDER BY products.id ASC
""")
    fun getAllProductFavourite(): PagingSource<Int, ProductWithFavouriteEntity>

    @Query("DELETE FROM productFavourites WHERE productId = :productId")
    suspend fun removeProductFromFavorites(productId: Int)
}