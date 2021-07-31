package com.abgp.colorcloud

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.abgp.colorcloud.models.WeatherData
import com.abgp.colorcloud.services.SharedPrefServices
import com.abgp.colorcloud.services.WeatherServices
import com.abgp.colorcloud.ui.theme.ColorTheme

class MainViewModel : ViewModel() {
    private val weatherServices = WeatherServices()

    val weatherData: LiveData<WeatherData?> = liveData {
        val data = weatherServices.getWeatherData()
        emit(data)
    }

    val theme: MutableLiveData<ColorTheme> by lazy {
        MutableLiveData<ColorTheme>(ColorTheme.ROSEANNA)
    }
}