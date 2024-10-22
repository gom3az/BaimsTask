package com.baimstask.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/*
* {
            "id": 1,
            "cityNameAr": "القاهرة",
            "cityNameEn": "Cairo",
            "lat": 30.0444,
            "lon": 31.2357
        }
* */
@Keep
@Serializable
@Parcelize
data class CityResource(
    val id: Int,
    val cityNameAr: String,
    val cityNameEn: String,
    val lat: Double,
    val lon: Double,
) : Parcelable