package com.example.mymoney.di

import android.content.Context
import androidx.room.Room
import com.example.mymoney.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_database"
            ).fallbackToDestructiveMigration(true)
            .build()
    }
}