package com.abgp.colorcloud.services

import android.location.Location
import com.abgp.colorcloud.ApiInterface
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

class WeatherServices {
    private val rfBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
        .create(ApiInterface::class.java)

    suspend fun getWeatherData(location: Location) = rfBuilder.getData(
        mapOf(
            "lat" to "${location.latitude}",
            "lon" to "${location.longitude}"
        )
    )
}