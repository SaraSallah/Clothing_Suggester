package com.example.clothing_suggester.data

import com.example.clothing_suggester.R

class SeasonalImageManager {

    fun getImageList(season: String): List<Int> {
        return when (season.toLowerCase()) {
            "summer" -> getSummerList()
            "autumn" -> getAutumnList()
            "winter" -> getWinterList()
            else -> throw IllegalArgumentException("Invalid season name: $season")
        }
    }

    private fun getSummerList(): List<Int> {
        return listOf(
            R.drawable.sum1,
            R.drawable.sum2,
            R.drawable.sum3,
            R.drawable.sum4,
            R.drawable.sum5,
            R.drawable.sum6,
            R.drawable.sum7,
            R.drawable.sum8,
        )
    }

    private fun getAutumnList(): List<Int> {
        return listOf(
            R.drawable.aut1,
            R.drawable.aut2,
            R.drawable.aut3,
            R.drawable.aut4,
            R.drawable.aut5,
            R.drawable.aut6,
//            R.drawable.aut7,
//            R.drawable.aut8
        )
    }

    private fun getWinterList(): List<Int> {
        return listOf(
            R.drawable.winter1,
            R.drawable.winter2,
            R.drawable.winter3,
            R.drawable.winter4,
            R.drawable.winter6
        )
    }
}
