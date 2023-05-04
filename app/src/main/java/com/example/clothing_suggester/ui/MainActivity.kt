package com.example.clothing_suggester.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.clothing_suggester.data.ClothesManager
import com.example.clothing_suggester.R
import com.example.clothing_suggester.data.model.MyLocation
import com.example.clothing_suggester.data.model.WeatherInfo
import com.example.clothing_suggester.data.source.LocationManager
import com.example.clothing_suggester.data.source.WeatherManager
import com.example.clothing_suggester.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationManager: LocationManager
    private lateinit var myLocation: MyLocation
    private val currentDate = Date()

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchBar.setOnQueryTextListener(this)
        getLocationCallBack()
        locationManager.checkPermission()
        getCurrentDate()

    }

    private fun getLocationCallBack() {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.v("KK", "$latitude lon $longitude")
//                    WeatherManager().getCurrentWeather(
//                        latitude,
//                        longitude,
//                        ::bindAPiDataInScreen,
//                        ::onError
//                    )
                    myLocation = MyLocation(latitude, longitude)
                    val cityName = getCityName(this@MainActivity, latitude, longitude)
                    Log.v("City", cityName)
                    WeatherManager().makeRequestUsingOKHTTP(
                        cityName, ::bindAPiDataInScreen,
                        ::onError
                    )

                }
            }
        }
        locationManager = LocationManager(this, locationCallback)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    fun getCurrentDate() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val formattedDate = dateFormat.format(currentDate)
        binding.date.text = "Today : $formattedDate"

    }
    fun getCityName(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context)
        try {
            val addressList = geocoder.getFromLocation(latitude, longitude, 1)
            if (addressList != null && addressList.isNotEmpty()) {
                return addressList[0].subAdminArea ?: ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        return "City not found"
    }

    override fun onStop() {
        super.onStop()
        // Stop location updates when the activity is stopped
        locationManager.stopLocationUpdates()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED && grantResults[1] ==
                PackageManager.PERMISSION_GRANTED
            ) {
                locationManager.getCurrentLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun callClothesManager(session: String) {
        val clothesManager = ClothesManager(this)
        clothesManager.saveClothesInSharedPreferences()
        clothesManager.getImageIds(session)
        val randomImageResourceId = clothesManager.getRandomImageResourceId(session)
        if (randomImageResourceId != null) {
            ContextCompat.getDrawable(this, randomImageResourceId)
            binding.imageViewClothes.setImageResource(randomImageResourceId)

        }
        clothesManager.removeUsedImageResourceId(session)

    }
    private fun clothesDependOnTemperature(temperature: Int): String {
        var finalSession = ""

        if (temperature in 15..20) finalSession = "autumn"
        if (temperature in 21..40) finalSession = "summer"
        if (temperature in 0..15) finalSession = "winter"

        return finalSession
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query.let {
            binding.apply {
                WeatherManager().makeRequestUsingOKHTTP(it!!, ::bindAPiDataInScreen, ::onError)
            }
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText!!.isEmpty()) {
            val cityName =
                getCityName(this@MainActivity, myLocation.latitude, myLocation.longitude)
            Log.v("City", cityName)
            WeatherManager().makeRequestUsingOKHTTP(
                cityName, ::bindAPiDataInScreen,
                ::onError
            )

        }

        return true
    }

    @SuppressLint("SetTextI18n")
    fun bindAPiDataInScreen(weather: WeatherInfo) {
        runOnUiThread {
            getTemperature(weather.main)
            callClothesManager(clothesDependOnTemperature(weather.temperature.toInt()))
            binding.textViewCity.text = weather.name
            binding.textViewTemp.text = "${weather.temperature}ْ C"
            binding.textViewMainWeather.text = weather.main
        }

    }
    private fun getTemperature(mainWeather: String) {
        when (mainWeather) {
            "Clear" -> binding.lottieAnimationViewCloud.setAnimation(R.raw.clear)
            "Rain" -> binding.lottieAnimationViewCloud.setAnimation(R.raw.shower)
            "Clouds" -> binding.lottieAnimationViewCloud.setAnimation(R.raw.cloud)
            "Mist" -> binding.lottieAnimationViewCloud.setAnimation(R.raw.mist)
            "Thunderstorm" -> binding.lottieAnimationViewCloud.setAnimation(R.raw.thunderstorm)
            "Snow" -> binding.lottieAnimationViewCloud.setAnimation(R.raw.snow)
        }
    }
    fun onError() {
        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()

    }


}
