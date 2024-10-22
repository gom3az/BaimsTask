package com.baimstask.data.repository

import com.baimstask.data.model.CityResource
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    fun observeAllCities(): Flow<List<CityResource>>
}