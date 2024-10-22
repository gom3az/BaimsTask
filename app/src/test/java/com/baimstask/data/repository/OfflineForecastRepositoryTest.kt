package com.baimstask.data.repository

import com.baimstask.data.database.dao.DailyForecastDao
import com.baimstask.data.model.City
import com.baimstask.data.model.ForecaseEntity
import com.baimstask.data.model.NetworkForecast
import com.baimstask.data.model.asExternal
import com.baimstask.data.model.asResource
import com.baimstask.data.model.toResource
import com.baimstask.data.remote.NetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.io.IOException

class OfflineForecastRepositoryTest {

    private val networkDataSource = mock(NetworkDataSource::class.java)
    private val dailyForecastDao = mock(DailyForecastDao::class.java)
    private val testDispatcher = Dispatchers.Unconfined

    private lateinit var SUT: OfflineForecastRepository

    @Before
    fun setUp() {
        SUT = OfflineForecastRepository(networkDataSource, dailyForecastDao, testDispatcher)
    }

    @Test
    fun `getByLatLon emits forecast from network and saves it to local database`() = runTest {
        // Arrange
        val networkForecast = NetworkForecast(City("eg", 1, "cairo"), 1, "test", emptyList(), 1)
        `when`(networkDataSource.getForecastByLatLon(10.0, 20.0)).thenReturn(networkForecast)

        // Act
        val resultFlow = SUT.getByLatLon(1, 10.0, 20.0)

        val result = resultFlow.first()

        // Assert
        assertEquals(networkForecast.toResource(), result)
        verify(dailyForecastDao).upsertForecastForCity(networkForecast.asExternal(1))
    }

    @Test
    fun `getByLatLon emits local forecast when network call fails`() = runTest {
        // Arrange
        val localForecast = ForecaseEntity(1, "cairo", "eg", emptyList())
        `when`(
            networkDataSource.getForecastByLatLon(
                10.0,
                20.0
            )
        ).thenThrow(IOException("Network error"))
        `when`(dailyForecastDao.getForecastById(1)).thenReturn(localForecast)

        // Act
        val resultFlow = SUT.getByLatLon(1, 10.0, 20.0)

        val result = resultFlow.first()

        // Assert
        assertEquals(localForecast.asResource(), result)
    }

    @Test
    fun `getByLatLon doesn't save to local database when network call fails`() = runTest {
        // Arrange
        `when`(
            networkDataSource.getForecastByLatLon(
                10.0,
                20.0
            )
        ).thenThrow(IOException("Network error"))
        val localForecast = ForecaseEntity(1, "cairo", "eg", emptyList())
        `when`(dailyForecastDao.getForecastById(1)).thenReturn(localForecast)

        // Act
        val resultFlow = SUT.getByLatLon(1, 10.0, 20.0)

        resultFlow.first()

        // Assert
        verify(dailyForecastDao, never()).upsertForecastForCity(localForecast)
    }
}
