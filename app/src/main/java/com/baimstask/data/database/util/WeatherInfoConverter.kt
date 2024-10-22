package com.baimstask.data.database.util

import androidx.room.TypeConverter
import com.baimstask.data.model.WeatherInfoEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WeatherInfoConverter {

    @TypeConverter
    fun fromWeatherInfoList(value: List<WeatherInfoEntity>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toWeatherInfoList(value: String): List<WeatherInfoEntity> {
        return Json.decodeFromString(value)
    }
}