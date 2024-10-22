package com.baimstask.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class NetworkForecast(
    val city: City, val cnt: Int, val cod: String, val list: List<WeatherDay>, val message: Int
)

@Keep
@Serializable
data class City(
    val country: String,
    val id: Int,
    val name: String,
)

@Keep
@Serializable
data class WeatherDay(
    val dt: Int, val main: Main, val weather: List<Weather>, val dt_txt: String
)

@Keep
@Serializable
data class Main(
    val humidity: Int, val temp: Double
)

@Keep
@Serializable
data class Weather(
    val description: String, val icon: String, val id: Int, val main: String
)

fun NetworkForecast.toResource() = ForecastResource(cityName = city.name,
    countryCode = city.country,
    weatherList = list.map { weatherDay ->
        WeatherInfo(
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