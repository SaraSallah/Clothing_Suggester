package com.example.clothing_suggester

import android.util.Log
import com.example.clothing_suggester.data.WeatherInfo
import com.example.clothing_suggester.data.WeatherResponse
import com.google.gson.Gson
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

class WeatherManager {

    private val client: OkHttpClient

    init {
        // Create OkHttpClient with logging interceptor for debug purposes
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }
    fun makeRequestUsingOKHTTP(
        city: String,
        onResult: (WeatherInfo) -> Unit,
        onFailure: () -> Unit,
    ) {
        val request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$API_KEY")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure()
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if (response.isSuccessful && !body.isNullOrEmpty()) {
                    val weatherResult = Gson().fromJson(body, WeatherResponse::class.java)
                    Log.e("TAG", weatherResult.toString())
                    val cityName = weatherResult.name
                    val temperatureInKelvin = weatherResult.main.temp
                    val temperature = convertTemperatureToCelsius(temperatureInKelvin)
                    val main = weatherResult.weather[0].main
                    val weatherData = WeatherInfo(cityName, temperature.toString(), main)
                    onResult(weatherData)
                }
            }
        })
    }

    fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        onResult: (WeatherInfo) -> Unit,
        onFailure: () -> Unit,
    ) {
        val request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$API_KEY")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure()
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if (response.isSuccessful && !body.isNullOrEmpty()) {
                    val weatherResult = Gson().fromJson(body, WeatherResponse::class.java)
                    Log.e("TAG", weatherResult.toString())
                    val cityName = weatherResult.name
                    val temperatureInKelvin = weatherResult.main.temp
                    val temperature = convertTemperatureToCelsius(temperatureInKelvin)
                    val main = weatherResult.weather[0].main
                    val weatherData = WeatherInfo(cityName, temperature.toString(), main)
                    onResult(weatherData)
                }
            }
        })
    }

    fun convertTemperatureToCelsius(temperatureInKelvin: Double): Int {
        val temperatureInCelsius = temperatureInKelvin - 273.15
        return temperatureInCelsius.toInt()
    }

    companion object {
        private const val API_KEY = "85a3186a733dc2ad66e00e5e5eaea084"
    }

}