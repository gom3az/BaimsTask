package com.baimstask.di

import android.content.Context
import androidx.room.Room
import com.baimstask.data.database.AppDatabase
import com.baimstask.data.database.dao.DailyForecastDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    fun providesForecastDao(
        database: AppDatabase,
    ): DailyForecastDao = database.dailyForecastDao()

    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "baims-database",
    ).build()
}