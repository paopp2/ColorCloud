package com.abgp.colorcloud

import com.abgp.colorcloud.models.WeatherData
import retrofit2.http.GET

interface ApiInterface {
    @GET("weather?q=cebu&units=metric&appid=bd2a5eb208e40d3b0413399e00438102")
    suspend fun getData() : WeatherData
}