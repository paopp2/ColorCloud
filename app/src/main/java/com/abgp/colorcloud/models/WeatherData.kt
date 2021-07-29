package com.abgp.colorcloud.models

data class Coord(val lat: Double, val lon: Double)
data class Main(val feels_like: Double, val humidity: Int, val pressure: Int, val temp: Double , val temp_max: Double, val temp_min: Double)
data class Sys(val country: String, val sunrise: Int, val sunset: Int, val type: Int)
data class Weather(val description: String, val icon: String, val main: String)
data class Wind(val speed: Double)

data class WeatherData(
    val cod: Int = 0,
    val coord: Coord = Coord(0.00,0.00),
    val dt: Int = 0,
    val id: Int = 0,
    val main: Main = Main(0.00,0,0,0.00,0.00,0.00),
    val name: String = "",
    val sys: Sys = Sys("",0,0,0),
    val timezone: Int = 0,
    val weather: List<Weather> = emptyList(),
    val wind: Wind = Wind(0.00)
)

