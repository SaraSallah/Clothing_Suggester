package com.example.clothing_suggester.data.model

import com.example.clothing_suggester.data.WeatherInformation
import com.example.clothing_suggester.data.WeatherMain

data class WeatherResponse(
    val name :String,
    val main : WeatherInformation,
    val weather : List<WeatherMain>
)

