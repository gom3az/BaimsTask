@file:OptIn(ExperimentalMaterial3Api::class)

package com.baimstask.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baimstask.R
import com.baimstask.data.model.CityResource
import com.baimstask.data.model.ForecastResource
import com.baimstask.data.model.WeatherInfo
import com.baimstask.ui.components.ErrorState
import com.baimstask.util.formattedDate

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            CityDropdownTopBar(
                cities = uiState.cities, onCityClicked = viewModel::onCityChanged
            )
        }) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is HomeUiState.Init -> {
                    if (state.isLoading) Box(
                        Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            trackColor = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }

                is HomeUiState.Success -> {
                    WeatherForecast(state.forecast, state.selectedCity)
                }

                is HomeUiState.Error -> {
                    ErrorState(viewModel::retryForecast)
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDropdownTopBar(
    cities: List<CityResource>, onCityClicked: (CityResource) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(title = {
        Text(text = stringResource(R.string.select_a_city))
    }, actions = {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown Menu")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            cities.forEach { city ->
                DropdownMenuItem(onClick = {
                    expanded = false

                    onCityClicked(city)
                }, text = { Text(text = city.cityNameEn) })
            }
        }
    }, modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun WeatherForecast(forecast: ForecastResource, selectedCity: CityResource) {
    Column(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (forecast.stale) {
            Text(
                text = stringResource(R.string.stale),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        // City Information
        CityInfo(city = selectedCity, countryCode = forecast.countryCode)

        // Weather days list
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            items(count = forecast.weatherList.size,
                key = { forecast.cityName + forecast.weatherList[it].dateTime }) { weatherDay ->
                WeatherDayItem(weatherDay = forecast.weatherList[weatherDay])
            }
        }
    }
}

@Composable
fun CityInfo(city: CityResource, countryCode: String) {
    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.city, city.cityNameEn, countryCode),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun WeatherDayItem(weatherDay: WeatherInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_small))
    ) {
        Text(
            text = stringResource(R.string.date_time, weatherDay.dateTime.formattedDate()),
            fontWeight = FontWeight.Bold
        )
        Text(text = stringResource(R.string.temperature_c, weatherDay.temperature))
        Text(text = stringResource(R.string.humidity, weatherDay.humidity))
        Text(text = stringResource(R.string.weather, weatherDay.weatherDescription))
    }
}

@Preview(showBackground = true)
@Composable
fun CityDropdownTopBarPreview() {
    CityDropdownTopBar(cities = emptyList()) {}
}

@Preview(showBackground = true)
@Composable
fun WeatherForecastPreview() {
    val cityName = "New York"
    val countryCode = "US"

    val weatherList = listOf(
        WeatherInfo(
            dateTime = "2024-10-21 12:00:00", temperature = 20.0, // Example temperature in Celsius
            humidity = 56, weatherDescription = "Clear sky"
        ), WeatherInfo(
            dateTime = "2024-11-22 15:00:00", temperature = 22.5, // Example temperature in Celsius
            humidity = 50, weatherDescription = "Partly cloudy"
        )
    )

    WeatherForecast(
        forecast = ForecastResource(
            stale = true,
            cityName = cityName, countryCode = countryCode, weatherList = weatherList
        ), selectedCity = CityResource(1, "test", "cairo", 1.1, 1.1)
    )
}
