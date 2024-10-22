package com.baimstask.data.repository

import com.baimstask.data.database.dao.DailyForecastDao
import com.baimstask.data.model.ForecastResource
import com.baimstask.data.model.asExternal
import com.baimstask.data.model.asResource
import com.baimstask.data.model.toResource
import com.baimstask.data.remote.NetworkDataSource
import com.baimstask.util.AppDispatchers
import com.baimstask.util.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class OfflineForecastRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val dailyForecastDao: DailyForecastDao,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ForecastRepository {

    override fun getByLatLon(id: Int, lat: Double, lon: Double): Flow<ForecastResource> = flow {
        val networkForecast = networkDataSource.getForecastByLatLon(lat, lon)
        emit(networkForecast.toResource())
        dailyForecastDao.upsertForecastForCity(networkForecast.asExternal(id))
    }.catch {
        val localForecast = dailyForecastDao.getForecastById(id)
        emit(localForecast.asResource())
    }.flowOn(ioDispatcher)
}