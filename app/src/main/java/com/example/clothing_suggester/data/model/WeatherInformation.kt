package com.example.clothing_suggester.data.model

import com.google.gson.annotations.SerializedName


data class WeatherInformation(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: String,
    @SerializedName("temp_min")val tempMin: String,
    @SerializedName("temp_max")val tempMax: String,
    @SerializedName("pressure")val pressure: String,
    @SerializedName("humidity") val humidity: String
)
data class WeatherMain(
    @SerializedName("id")val id: String,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon")val icon: String
)




