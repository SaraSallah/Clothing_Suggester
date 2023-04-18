package com.example.clothing_suggester

import android.content.Context
import android.content.SharedPreferences
import com.example.clothing_suggester.data.SeasonalImageManager
import com.example.clothing_suggester.util.Constant

class ClothesManager(context : Context) {
    val seasonalImageManager = SeasonalImageManager()

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constant.SHARED_PREF_MAME, Context.MODE_PRIVATE)
    private val summerList = listOf<Int>(
        R.drawable.sum1,
        R.drawable.sum2,
        R.drawable.sum3,
    )

    fun saveClothesInSharedPreferenc() {
        val autumnList = seasonalImageManager.getImageList("autumn").joinToString(",")
        val summList = seasonalImageManager.getImageList("summer").joinToString(",")
        val winterList = seasonalImageManager.getImageList("winter").joinToString(",")

        val editor = sharedPreferences.edit()
        editor.putString(Constant.AUTUMN, autumnList)
        editor.putString(Constant.SUMMER, summList)
        editor.putString(Constant.WINTER, winterList)
        editor.commit()
    }

    fun getImageIds(key: String): List<Int> {
        val imageIdsString = sharedPreferences.getString(key, "")
        return if (imageIdsString.isNullOrEmpty()) {
            emptyList()
        } else {
            imageIdsString.split(",").map { it.toInt() }
        }
    }

    // Function to get a random image resource ID from the shuffled array
    fun getRandomImageResourceId(key: String): Int? {
        val imageIds = getImageIds(key)
        if (imageIds.isEmpty()) {
            return null
        }
        val shuffledIds = imageIds.shuffled()
        return shuffledIds.firstOrNull()
    }

    fun removeUsedImageResourceId(key: String) {
        val imageIds = getImageIds(key).toMutableList()
        if (imageIds.isNotEmpty()) {
            imageIds.removeAt(0)
            val editor = sharedPreferences.edit()
            editor.putString(
                Constant.KEY,
                imageIds.joinToString(",")
            )
            editor.apply()
        }
    }

    }


