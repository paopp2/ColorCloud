package com.abgp.colorcloud

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.abgp.colorcloud.models.WeatherData
import com.abgp.colorcloud.services.WeatherServices

class MainViewModel : ViewModel() {
    private val weatherServices = WeatherServices()

    val weatherData: LiveData<WeatherData?> = liveData {
        val data = weatherServices.getWeatherData()
        emit(data)
    }
}