package com.example.clothing_suggester.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val name :String,
    val main : WeatherInformation,
    val weather : List<WeatherMain>
)

