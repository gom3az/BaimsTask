package com.baimstask.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baimstask.data.model.CityResource
import com.baimstask.data.model.ForecastResource
import com.baimstask.data.repository.CityRepository
import com.baimstask.data.repository.ForecastRepository
import com.baimstask.ui.home.HomeUiState.Init
import com.baimstask.ui.home.HomeUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    cityRepository: CityRepository, forecastRepository: ForecastRepository
) : ViewModel() {

    // Search query for city selection
    private val cityQuery = MutableStateFlow<CityResource?>(null)

    val uiState: StateFlow<HomeUiState> =
        cityRepository.observeAllCities().flatMapLatest { cityList ->
            cityQuery.filterNotNull().flatMapLatest { selectedCity ->
                forecastRepository.getByLatLon(selectedCity.id, selectedCity.lat, selectedCity.lon)
                    .map<ForecastResource, HomeUiState> { forecastResource ->
                        Success(cityList, forecastResource, selectedCity)
                    }.onStart {
                        emit(Init(cityList, true)) // While loading the forecast
                    }.catch { forecastError ->
                        emit(
                            HomeUiState.Error(
                                cityList, forecastError.message ?: "Failed to load forecast"
                            )
                        )
                    }
            }.onStart { emit(Init(cityList)) } // Initial loading of the city list
                .catch { citiesError ->
                    emit(
                        HomeUiState.Error(
                            cityList, citiesError.message ?: "Failed to load cities"
                        )
                    )
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Init(emptyList())
        )

    fun onCityChanged(newCity: CityResource) {
        cityQuery.value = newCity // Directly set the new city
    }

    fun retryForecast() {
        cityQuery.value?.let { currentCity ->
            cityQuery.value = null   // Emit null to trigger loading again
            cityQuery.value = currentCity // Set back to the current city to retry
        }
    }
}

sealed class HomeUiState(open val cities: List<CityResource>) {
    data class Init(override val cities: List<CityResource>, val isLoading: Boolean = false) :
        HomeUiState(cities)

    data class Success(
        override val cities: List<CityResource>,
        val forecast: ForecastResource,
        val selectedCity: CityResource
    ) : HomeUiState(cities)

    data class Error(
        override val cities: List<CityResource>, val message: String
    ) : HomeUiState(cities)
}
