package com.baimstask.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.baimstask.data.model.ForecaseEntity

@Dao
interface DailyForecastDao {
    @Query(
        value = """
        SELECT * FROM forecasts
        WHERE id = :cityId
    """,
    )
    fun getForecastById(cityId: Int): ForecaseEntity

    @Upsert
    suspend fun upsertForecastForCity(forecast: ForecaseEntity)

}