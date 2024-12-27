package com.example.nexmedis_test.dependencyInjection

import android.content.Context
import androidx.room.Room
import com.example.nexmedis_test.database.MainDB
import com.example.nexmedis_test.database.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): MainDB {
        return Room.databaseBuilder(
            context,
            MainDB::class.java,
            "main_db"
        ).build()
    }

    @Provides
    fun provideProductDao(database: MainDB): ProductDao {
        return database.productDAO()
    }
}