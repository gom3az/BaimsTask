package com.baimstask.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.baimstask.data.model.CityResource
import com.baimstask.data.model.ForecastResource
import com.baimstask.data.model.WeatherInfo
import com.baimstask.util.formattedDate
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherAppTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Sample data for testing
    private val sampleCities = listOf(
        CityResource(1, "Cairo", "Cairo", 1.1, 1.1),
        CityResource(2, "London", "London", 1.1, 1.1),
        CityResource(3, "New York", "New York", 1.1, 1.1),
    )

    private val sampleForecast = ForecastResource(
        countryCode = "EG", cityName = "Cairo", weatherList = listOf(
            WeatherInfo(
                dateTime = "2024-10-21 10:00:00",
                temperature = 25.6,
                humidity = 60,
                weatherDescription = "Clear"
            ), WeatherInfo(
                dateTime = "2024-10-22 10:00:00",
                temperature = 27.5,
                humidity = 55,
                weatherDescription = "Sunny"
            )
        )
    )

    @Test
    fun testCityDropdownTopBar() {
        composeTestRule.setContent {
            CityDropdownTopBar(cities = sampleCities) { selectedCity ->
                // You can assert that the city selection logic is handled correctly here
            }
        }

        // Check if the dropdown is initially closed
        composeTestRule.onNodeWithContentDescription("Dropdown Menu").assertIsDisplayed()

        // Click the dropdown button
        composeTestRule.onNodeWithContentDescription("Dropdown Menu").performClick()

        // Check if the city options are displayed
        sampleCities.forEach { city ->
            composeTestRule.onNodeWithText(city.cityNameEn).assertIsDisplayed()
        }

        // Select a city
        composeTestRule.onNodeWithText("Cairo").performClick()

    }

    @Test
    fun testCitySelection() {
        var selectedCity: CityResource? = null
        val cities = listOf(
            CityResource(1, "Cairo", "Cairo", 1.1, 1.1),
            CityResource(2, "Giza", "Giza", 1.1, 1.1),
            CityResource(3, "London", "London", 1.1, 1.1),
        )

        composeTestRule.setContent {
            CityDropdownTopBar(cities) { city ->
                selectedCity = city
            }
        }

        // Open dropdown and select a city
        composeTestRule.onNodeWithContentDescription("Dropdown Menu").performClick()
        composeTestRule.onNodeWithText("Cairo").performClick()

        // Verify that the selected city is as expected
        assert(selectedCity == cities[0]) { "Expected city to be Cairo" }
    }

    @Test
    fun testWeatherForecastDisplaysCorrectly() {
        composeTestRule.setContent {
            WeatherForecast(forecast = sampleForecast, selectedCity = sampleCities[0])
        }

        // Check if the city info is displayed
        composeTestRule.onNodeWithText("City: Cairo, EG").assertIsDisplayed()

        // Check if the weather days are displayed
        sampleForecast.weatherList.forEach { weatherDay ->
            composeTestRule.onNodeWithText("Date/Time: ${weatherDay.dateTime.formattedDate()}")
                .assertIsDisplayed()
            composeTestRule.onNodeWithText("Temperature: ${weatherDay.temperature}°C")
                .assertIsDisplayed()
            composeTestRule.onNodeWithText("Humidity: ${weatherDay.humidity}%").assertIsDisplayed()
            composeTestRule.onNodeWithText("Weather: ${weatherDay.weatherDescription}")
                .assertIsDisplayed()
        }
    }
}