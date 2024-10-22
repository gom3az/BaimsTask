package com.baimstask.data.remote

import com.baimstask.BuildConfig
import com.baimstask.data.model.NetworkForecast
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

interface RetrofitNetworkApi {

    @GET(value = "/data/2.5/forecast")
    suspend fun getForecastByLatLon(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
    ): NetworkForecast
}

private const val BASE_URL = BuildConfig.BACKEND_URL
private const val FORECAST_APPID = BuildConfig.APPID

@Singleton
internal class RetrofitNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : NetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .callFactory { okhttpCallFactory.get().newCall(it) }
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitNetworkApi::class.java)

    override suspend fun getForecastByLatLon(lat: Double, lon: Double): NetworkForecast =
        networkApi.getForecastByLatLon(lat = lat, lon = lon, appid = FORECAST_APPID)

}