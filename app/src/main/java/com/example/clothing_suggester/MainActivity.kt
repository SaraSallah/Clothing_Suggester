package com.example.clothing_suggester

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
        WeatherManager().makeRequestUsingOKHTTP("Cairo", ::ui, ::onError)
        binding.searchBar.setOnQueryTextListener(this)

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

    val summerList = listOf<Int>(
        R.drawable.summ1,
        R.drawable.summ2,
        R.drawable.summ3,
    )
    val autList = listOf<Int>(
        R.drawable.aut1,
        R.drawable.aut2,
        R.drawable.aut3,
        R.drawable.aut4,
        R.drawable.aut5,
        R.drawable.aut6,
        R.drawable.aut7,
    )
    val winterList = listOf<Int>(
        R.drawable.winter1,
        R.drawable.winter2,
        R.drawable.winter3,
        R.drawable.winter4,
        R.drawable.winter5
    )

    fun clothesDependOnTemperature(temperatur: Int): List<Int> {
        var finalList = emptyList<Int>()

        if (temperatur in 15..20)
            finalList = autList
        if (temperatur in 25..40)
            finalList = summerList
        if (temperatur in 0..15)
            finalList = winterList

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
        if(newText!!.isEmpty())
            WeatherManager().makeRequestUsingOKHTTP("Cairo",::ui, ::onError)
        return true
    }
    fun ui(weather: WeatherInfo) {
        runOnUiThread {
            getTemperature(weather.main)
            callClothesManager(clothesDependOnTemperature(weather.temperature.toInt()))
            binding.textViewCity.text = weather.name
            binding.textViewTemp.text = weather.temperature+"ْ C"
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


