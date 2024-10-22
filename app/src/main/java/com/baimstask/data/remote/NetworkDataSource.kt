package com.baimstask.data.remote

import com.baimstask.data.model.NetworkForecast
import java.io.IOException

interface NetworkDataSource {

    @Throws(IOException::class)
    suspend fun getForecastByLatLon(lat: Double, lon: Double): NetworkForecast
}