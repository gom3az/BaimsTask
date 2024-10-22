package com.baimstask.ui.home

import app.cash.turbine.test
import com.baimstask.MainDispatcherRule
import com.baimstask.data.model.CityResource
import com.baimstask.data.model.ForecastResource
import com.baimstask.data.repository.CityRepository
import com.baimstask.data.repository.ForecastRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val cityRepository: CityRepository = mock(CityRepository::class.java)
    private val forecastRepository: ForecastRepository = mock(ForecastRepository::class.java)

    @Test
    fun `test loading cities success`() = runTest {

        // Arrange
        val cities = listOf(CityResource(1, "", "City1", 10.0, 20.0))
        `when`(cityRepository.observeAllCities()).thenReturn(flow { emit(cities) })

        // Act
        val SUT = HomeViewModel(cityRepository, forecastRepository)
        // Assert
        val expectedState = HomeUiState.Init(cities, false)
        assertEquals(expectedState, SUT.uiState.first())
    }

    @Test
    fun `test loading cities and forecast success`() = runTest {

        // Arrange
        val cities = listOf(CityResource(1, "", "City1", 10.0, 20.0))
        val forecast = ForecastResource("Clear", "", emptyList())

        `when`(cityRepository.observeAllCities()).thenReturn(flow { emit(cities) })
        `when`(forecastRepository.getByLatLon(1, 10.0, 20.0)).thenReturn(flow { emit(forecast) })

        val SUT = HomeViewModel(cityRepository, forecastRepository)
        // Act
        SUT.onCityChanged(cities[0])
        // Assert
        val expectedState = HomeUiState.Success(cities, forecast, cities[0])
        SUT.uiState.test {
            assertEquals(expectedState, expectMostRecentItem())
        }
    }

    @Test
    fun `test loading cities and forecast failure`() = runTest {
        // Arrange
        val cities = listOf(CityResource(1, "City1", "", 10.0, 20.0))

        `when`(cityRepository.observeAllCities()).thenReturn(flow { emit(cities) })
        `when`(
            forecastRepository.getByLatLon(1, 10.0, 20.0)
        ).thenReturn(flow { throw Exception("Forecast error") })

        val SUT = HomeViewModel(cityRepository, forecastRepository)
        // Act
        SUT.onCityChanged(cities[0])

        // Assert
        val expectedState = HomeUiState.Error(cities, "Forecast error")
        SUT.uiState.test {
            assertEquals(expectedState, expectMostRecentItem())
        }
    }

    @Test
    fun `test retry forecast`() = runTest {
        // Arrange
        val cities = listOf(CityResource(1, "City1", "", 10.0, 20.0))
        val forecast = ForecastResource("Clear", "25", listOf())

        `when`(cityRepository.observeAllCities()).thenReturn(flow { emit(cities) })
        `when`(
            forecastRepository.getByLatLon(1, 10.0, 20.0)
        ).thenReturn(flow { throw Exception("Forecast error") }).thenReturn(flow { emit(forecast) })

        val SUT = HomeViewModel(cityRepository, forecastRepository)
        // selecting a city
        SUT.onCityChanged(cities[0])

        // Simulate an error in forecast
        SUT.onCityChanged(cities[0]) // This would trigger an error

        // Verify that the state is Error
        SUT.uiState.test {
            assertEquals(HomeUiState.Error(cities, "Forecast error"), expectMostRecentItem())
        }
        // Act
        SUT.retryForecast()

        // Assert
        SUT.uiState.test {
            assertEquals(HomeUiState.Success(cities, forecast, cities[0]), expectMostRecentItem())
        }
    }
}