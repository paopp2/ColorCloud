package com.abgp.colorcloud

data class Coord(
    val lat: Double = 0.00,
    val lon: Double = 0.00
)
data class Main(
    val feels_like: Double = 0.00,
    val humidity: Int = 0,
    val pressure: Int = 0,
    val temp: Double = 0.00,
    val temp_max: Double = 0.00,
    val temp_min: Double = 0.00
)
data class Sys(
    val country: String = "",
    val sunrise: Int = 0,
    val sunset: Int = 0,
    val type: Int = 0
)
data class Weather(
    val description: String = "",
    val icon: String = "",
    val main: String = ""
)
data class Wind(
    val speed: Double = 0.00
)

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
