package com.baimstask.data.repository

import com.baimstask.data.model.ForecastResource
import kotlinx.coroutines.flow.Flow

interface ForecastRepository {
    fun getByLatLon(id: Int, lat: Double, lon: Double): Flow<ForecastResource>
}