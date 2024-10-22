package com.baimstask.data.local

import com.baimstask.data.model.CityEntity
import com.baimstask.data.model.CityEntityList
import com.baimstask.data.model.CitySerializer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DefaultFileDataSource @Inject constructor(
    private val assetManager: AssetManager, private val ioDispatcher: CoroutineDispatcher
) : FileDataSource {

    companion object {
         const val CITIES_ASSET = "cities.json"
    }

    override suspend fun getCities(): Result<List<CityEntity>> = withContext(ioDispatcher) {
        try {
            val inputStream = assetManager.open(CITIES_ASSET)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val data: CityEntityList = Json.decodeFromString(CitySerializer, jsonString)
            Result.success(data.cities)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}