package com.example.clothing_suggester.data.model

data class WeatherResponse(
    val name :String,
    val main : WeatherInformation,
    val weather : List<WeatherMain>
)

