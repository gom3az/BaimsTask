package com.baimstask.data.model

import androidx.annotation.Keep
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

@Keep
@Serializable
data class CityEntityList(val cities: List<CityEntity>)

@Keep
@Serializable
data class CityEntity(
    val id: Int,
    val cityNameAr: String,
    val cityNameEn: String,
    val lat: Double,
    val lon: Double,
)

@OptIn(InternalSerializationApi::class)
val CitySerializer = CityEntityList::class.serializer()

fun CityEntity.toResource() = CityResource(
    id = id,
    cityNameAr = cityNameAr,
    cityNameEn = cityNameEn,
    lat = lat,
    lon = lon
)