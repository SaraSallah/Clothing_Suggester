package com.example.clothing_suggester.data

import com.google.gson.annotations.SerializedName

data class WeatherInformation(
    val temp: Double,
    val feels_like: String,
    val temp_min: String,
    val temp_max: String,
    val pressure: String,
    val humidity: String
)
data class WeatherMain(
    val id: String,
    val main: String,
    val description: String,
    val icon: String
)




