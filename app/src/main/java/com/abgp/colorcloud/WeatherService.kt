package com.abgp.colorcloud

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

object WeatherService {

    @DelicateCoroutinesApi
    private fun fetchWeatherData(onDataFetched: ()->Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val resWeatherData: WeatherData = getWeatherData()
            Log.d("ResMain: ",resWeatherData.toString())

            withContext(Dispatchers.Main){
                onDataFetched()
            }
        }

    }

    private suspend fun getWeatherData(): WeatherData {
        var apiResponse = WeatherData()
        val rfBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        // getData() from ApiInterface
        val rfData = rfBuilder.getData()

        rfData.enqueue(object : Callback<WeatherData?> {
            override fun onResponse(call: Call<WeatherData?>, response: Response<WeatherData?>) {
                val resBody: WeatherData = response.body()!!
                apiResponse = resBody
            }

            override fun onFailure(call: Call<WeatherData?>, t: Throwable) {
                // Search the tag in logcat
                Log.d("Response Err: ",t.message.toString())
            }
        })
        Log.d("Return apiResponse: ", apiResponse.toString())
         delay(3000L)
        return apiResponse
    }
}