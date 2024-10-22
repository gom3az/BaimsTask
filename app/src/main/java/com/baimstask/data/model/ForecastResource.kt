package com.baimstask.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ForecastResource(
    val cityName: String,
    val countryCode: String,
    val weatherList: List<WeatherInfo>,
    val stale: Boolean = false
)

@Keep
@Serializable
data class WeatherInfo(
    val dateTime: String,
    val temperature: Double,
    val humidity: Int,
    val weatherDescription: String
)