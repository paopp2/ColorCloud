package com.abgp.colorcloud

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

object WeatherService {

    fun getWeatherData(): WeatherData {
        var apiResponse = WeatherData()
        val rfBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        // getData() from ApiInterface
        val rfData = rfBuilder.getData()

        /* USING COROUTINES
        GlobalScope.launch(Dispatchers.IO){
            val resBody = rfBuilder.getData().await()
            Log.d("Response: ", resBody.toString())
            apiResponse = resBody
        }*/

        rfData.enqueue(object : Callback<WeatherData?> {
            override fun onResponse(call: Call<WeatherData?>, response: Response<WeatherData?>) {
                val resBody = response.body()!!
                // Search the tag in logcat
                apiResponse = resBody
                Log.d("apiResponse: ", apiResponse.toString())
            }

            override fun onFailure(call: Call<WeatherData?>, t: Throwable) {
                // Search the tag in logcat
                Log.d("Response Err: ",t.message.toString())
            }
        })
        Log.d("Return apiResponse: ", apiResponse.toString())

        return apiResponse
    }



}