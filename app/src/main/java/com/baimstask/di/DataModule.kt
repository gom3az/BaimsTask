package com.baimstask.di

import com.baimstask.data.repository.CityRepository
import com.baimstask.data.repository.DefaultCityRepository
import com.baimstask.data.repository.OfflineForecastRepository
import com.baimstask.data.repository.ForecastRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsCitiesRepository(
        citiesRepository: DefaultCityRepository,
    ): CityRepository

    @Binds
    internal abstract fun bindsForecastRepository(
        forecastRepository: OfflineForecastRepository,
    ): ForecastRepository

}