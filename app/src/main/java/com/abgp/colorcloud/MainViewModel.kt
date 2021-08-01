package com.abgp.colorcloud

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.view.View
import androidx.lifecycle.*
import com.abgp.colorcloud.models.WeatherData
import com.abgp.colorcloud.services.SharedPrefServices
import com.abgp.colorcloud.services.WeatherServices
import com.abgp.colorcloud.ui.theme.ColorTheme

class MainViewModel : ViewModel() {
    private val weatherServices = WeatherServices()

    val geoData : MutableLiveData<Location> = MutableLiveData()

    val weatherData: LiveData<WeatherData> = geoData.switchMap { loc ->
        liveData {
            val data = weatherServices.getWeatherData(loc)
            emit(data)
        }
    }

    val theme: MutableLiveData<ColorTheme> by lazy {
        MutableLiveData<ColorTheme>(ColorTheme.ROSEANNA)
    }
}