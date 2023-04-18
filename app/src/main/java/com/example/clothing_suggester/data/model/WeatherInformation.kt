package com.example.clothing_suggester.data

import com.google.gson.annotations.SerializedName


data class WeatherInformation(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feels_like: String,
    @SerializedName("temp_min")val temp_min: String,
    @SerializedName("temp_max")val temp_max: String,
    @SerializedName("pressure")val pressure: String,
    @SerializedName("humidity") val humidity: String
)
data class WeatherMain(
    @SerializedName("id")val id: String,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon")val icon: String
)




