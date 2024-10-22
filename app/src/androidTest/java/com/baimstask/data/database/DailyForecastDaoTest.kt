package com.baimstask.data.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.baimstask.data.database.dao.DailyForecastDao
import com.baimstask.data.model.ForecaseEntity
import com.baimstask.data.model.WeatherInfoEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class) // Use the AndroidJUnit4 runner for testing Room
class DailyForecastDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var SUT: DailyForecastDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), AppDatabase::class.java
        ).build()

        SUT = database.dailyForecastDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndGetForecastById() = runTest {
        // Arrange
        val forecast = ForecaseEntity(
            1, "cairo", "eg", listOf(WeatherInfoEntity("2024-10-21 12:00:00", 11.0, 11, "desc"))
        )

        // Act
        SUT.upsertForecastForCity(forecast)

        // Assert
        val result = SUT.getForecastById(1)
        assertEquals(forecast, result)
    }

    @Test
    fun testUpdatingExistingForecast() = runTest {
        // Arrange
        val forecast = ForecaseEntity(
            1, "cairo", "eg", listOf(WeatherInfoEntity("2024-10-21 12:00:00", 11.0, 11, "desc"))
        )
        SUT.upsertForecastForCity(forecast)

        // Act
        val updatedForecast = forecast.copy(
            weatherList = listOf(
                WeatherInfoEntity(
                    "2024-10-21 12:00:00", 16.0, 11, "desc"
                )
            )
        )
        SUT.upsertForecastForCity(updatedForecast)

        // Assert
        val result = SUT.getForecastById(1)
        assertEquals(updatedForecast, result)
    }
}
