package com.baimstask.data.local

import com.baimstask.data.model.CityEntity

interface FileDataSource {
    suspend fun getCities(): Result<List<CityEntity>>
}