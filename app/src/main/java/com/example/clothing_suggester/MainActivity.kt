package com.example.clothing_suggester

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.clothing_suggester.data.LocationManager
import com.example.clothing_suggester.data.MyLocation
import com.example.clothing_suggester.data.SeasonalImageManager
import com.example.clothing_suggester.data.WeatherInfo
import com.example.clothing_suggester.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationManager: LocationManager
    private lateinit var myLoction: MyLocation
    val currentDate = Date()

    val seasonalImageManager = SeasonalImageManager()

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchBar.setOnQueryTextListener(this)
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.v("KK", "$latitude lon $longitude")
                    WeatherManager().getCurrentWeather(latitude, longitude, ::ui, ::onError)
                    myLoction = MyLocation(latitude, longitude)
                }
            }
        }
        locationManager = LocationManager(this, locationCallback)
        locationManager.checkPermision()
        getCurrentDate()

    }

    fun getCurrentDate() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val formattedDate = dateFormat.format(currentDate)
        binding.date.text = "Today : $formattedDate"

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
        if (requestCode == MainActivity.REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // If location permission is granted, get current location
                locationManager.getCurrentLocation()
            } else {
                // If location permission is denied, show a toast message
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun callClothesManager(list: List<Int>) {
        val clothesManager = ClothesManager(this)
        clothesManager.saveClothesInSharedPreferenc(list)
        val imageIds = clothesManager.getImageIds()
        val randomImageResourceId = clothesManager.getRandomImageResourceId()
        if (randomImageResourceId != null) {
            val randomDrawable = ContextCompat.getDrawable(this, randomImageResourceId)
            // Set the randomDrawable to your ImageView or any other appropriate UI element
            binding.imageViewClothes.setImageResource(randomImageResourceId)

        }
        clothesManager.removeUsedImageResourceId()

    }
    fun clothesDependOnTemperature(temperatur: Int): List<Int> {
        var finalList = emptyList<Int>()

        if (temperatur in 15..20) finalList = seasonalImageManager.getImageList("autumn")
        if (temperatur in 21..40) finalList = seasonalImageManager.getImageList("summer")
        if (temperatur in 0..15) finalList = seasonalImageManager.getImageList("winter")

        return finalList
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query.let {
            binding.apply {
                WeatherManager().makeRequestUsingOKHTTP(it!!, ::ui, ::onError)
            }
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText!!.isEmpty()) {
            WeatherManager().getCurrentWeather(
                myLoction.latitude,
                myLoction.longitude,
                ::ui,
                ::onError
            )
        }

        return true
    }

    fun ui(weather: WeatherInfo) {
        runOnUiThread {
            getTemperature(weather.main)
            callClothesManager(clothesDependOnTemperature(weather.temperature.toInt()))
            binding.textViewCity.text = weather.name
            binding.textViewTemp.text = weather.temperature
            binding.textViewMainWeather.text = weather.main
        }

    }
    fun getTemperature(mainWeather: String) {
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
