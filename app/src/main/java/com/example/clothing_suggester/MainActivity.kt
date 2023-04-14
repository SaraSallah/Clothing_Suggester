package com.example.clothing_suggester

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clothing_suggester.data.WeatherInfo
import com.example.clothing_suggester.databinding.ActivityMainBinding
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityMainBinding
    val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WeatherManager().makeRequestUsingOKHTTP("Helwan",::ui, ::onError)
        binding.searchBar.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        WeatherManager().makeRequestUsingOKHTTP(query!!,::ui, ::onError)
        return true
    }


    override fun onQueryTextChange(newText: String?): Boolean {
       // WeatherManager().makeRequestUsingOKHTTP(newText!!, ::ui, ::onError)
        return true
    }
    fun ui(weather: WeatherInfo) {
        runOnUiThread {
            getTemperature(weather.main)
            binding.textViewCity.text = weather.name
            binding.textViewTemp.text = "$weather.temperature C"
            binding.textViewMainWeather.text = weather.main
        }

    }
    fun getTemperature(mainWeather: String) {
        when (mainWeather) {
            "Clear" ->{
                binding.let {
                    it.lottieAnimationViewSunny.visibility = View.VISIBLE
                it.lottieAnimationViewCloud.visibility = View.GONE
                it.lottieAnimationViewShower.visibility = View.GONE
                }
            }
            "Rain" ->
                binding.let {
                    it.lottieAnimationViewSunny.visibility = View.GONE
                    it.lottieAnimationViewCloud.visibility = View.GONE
                    it.lottieAnimationViewShower.visibility = View.VISIBLE
                }
            "Clouds" ->
                    binding.let {
                it.lottieAnimationViewSunny.visibility = View.GONE
                it.lottieAnimationViewCloud.visibility = View.VISIBLE
                it.lottieAnimationViewShower.visibility = View.GONE
            }
        }
    }
    fun onError() {
        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()

    }

}


