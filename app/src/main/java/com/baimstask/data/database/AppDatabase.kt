package com.baimstask.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.baimstask.data.database.dao.DailyForecastDao
import com.baimstask.data.database.util.WeatherInfoConverter
import com.baimstask.data.model.ForecaseEntity

@Database(
    entities = [
        ForecaseEntity::class,
    ],
    version = 1,
)
@TypeConverters(WeatherInfoConverter::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun dailyForecastDao(): DailyForecastDao
}