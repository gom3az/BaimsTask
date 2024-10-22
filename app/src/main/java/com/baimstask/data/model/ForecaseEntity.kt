package com.baimstask.data.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "forecasts",
)
@Keep
@Serializable
data class ForecaseEntity(
    @PrimaryKey val id: Int,
    val cityName: String,
    val countryCode: String,
    val weatherList: List<WeatherInfoEntity>
)

@Keep
@Serializable
data class WeatherInfoEntity(
    val dateTime: String, val temperature: Double, val humidity: Int, val weatherDescription: String
)

fun NetworkForecast.asExternal(id: Int) = ForecaseEntity(cityName = city.name,
    countryCode = city.country,
    id = id,
    weatherList = list.map { weatherDay ->
        WeatherInfoEntity(
            dateTime = weatherDay.dt_txt,
            temperature = weatherDay.main.temp - 273.15, // Convert Kelvin to Celsius
            humidity = weatherDay.main.humidity,
            weatherDescription = if (weatherDay.weather.isNotEmpty()) {
                weatherDay.weather[0].description
            } else {
                "No description available"
            }
        )
    })

fun ForecaseEntity.asResource() = ForecastResource(stale = true,
    cityName = cityName,
    countryCode = countryCode,
    weatherList = weatherList.map {
        WeatherInfo(
            dateTime = it.dateTime,
            temperature = it.temperature,
            humidity = it.humidity,
            weatherDescription = it.weatherDescription
        )
    })