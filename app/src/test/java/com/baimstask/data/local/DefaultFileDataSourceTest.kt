package com.baimstask.data.local

import com.baimstask.MainDispatcherRule
import com.baimstask.data.model.CityEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.io.IOException
import java.io.InputStream

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultFileDataSourceTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val assetManager = mock(AssetManager::class.java)
    private val testDispatcher = Dispatchers.Unconfined

    private lateinit var SUT: DefaultFileDataSource

    @Before
    fun setUp() {
        SUT = DefaultFileDataSource(assetManager, testDispatcher)
    }

    @Test
    fun `test getCities returns success`() = runTest {
        // Arrange
        val mockInputStream: InputStream = "fake json string".byteInputStream()
        `when`(assetManager.open("cities.json")).thenReturn(mockInputStream)

        //
        val mockJson = """
            {
              "cities": [
                { "id": 1, "cityNameAr": "City1", "cityNameEn": "City1", "lat": 10.0, "lon": 20.0 }
              ]
            }
        """.trimIndent()

        `when`(assetManager.open(DefaultFileDataSource.CITIES_ASSET)).thenReturn(mockJson.byteInputStream())

        // Act
        val result = SUT.getCities()

        // Assert
        val expectedCityList = listOf(CityEntity(1, "City1", "City1", 10.0, 20.0))
        assertEquals(Result.success(expectedCityList), result)
    }

    @Test
    fun `test getCities returns failure when exception occurs`() = runTest {
        // Arrange
        doThrow(IOException("File not found")).`when`(assetManager)
            .open(DefaultFileDataSource.CITIES_ASSET)

        // Act
        val result = SUT.getCities()

        // Assert
        assert(result.isFailure)
        assertEquals("File not found", result.exceptionOrNull()?.message)
    }
}