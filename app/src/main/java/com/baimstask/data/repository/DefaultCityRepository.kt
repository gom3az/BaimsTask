package com.baimstask.data.repository

import com.baimstask.data.local.FileDataSource
import com.baimstask.data.model.CityResource
import com.baimstask.data.model.toResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultCityRepository @Inject constructor(
    private val fileDataSource: FileDataSource
) : CityRepository {

    override fun observeAllCities(): Flow<List<CityResource>> = flow {
        val result = fileDataSource.getCities()
        emit(result.getOrDefault(listOf()).map { it.toResource() })
    }
}