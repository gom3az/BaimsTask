package com.baimstask.data.repository

import com.baimstask.data.local.FileDataSource
import com.baimstask.data.model.CityEntity
import com.baimstask.data.model.CityResource
import com.baimstask.data.model.toResource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*

class DefaultCityRepositoryTest {

    private val fileDataSource: FileDataSource = mock(FileDataSource::class.java)

    private val SUT = DefaultCityRepository(fileDataSource)

    @Test
    fun `test observeAllCities emits cities successfully`() = runTest {
        // Arrange
        val cityEntities = listOf(CityEntity(1, "City1", "City1", 10.0, 20.0))
        `when`(fileDataSource.getCities()).thenReturn(Result.success(cityEntities))

        // Act
        val flow = SUT.observeAllCities()
        val emittedCities = flow.first()

        // Assert
        val expectedCities = cityEntities.map { it.toResource() }
        assertEquals(expectedCities, emittedCities)
    }

    @Test
    fun `test observeAllCities emits empty list when fileDataSource fails`() = runTest {
        // Arrange
        `when`(fileDataSource.getCities()).thenReturn(Result.failure(Exception("Error loading cities")))

        // Act
        val flow = SUT.observeAllCities()
        val emittedCities = flow.first()

        // Assert
        assertEquals(emptyList<CityResource>(), emittedCities)
    }
}
