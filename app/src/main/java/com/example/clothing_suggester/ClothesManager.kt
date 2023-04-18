package com.example.clothing_suggester

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.clothing_suggester.util.Constants

class ClothesManager(context : Context) {
    companion object {
        private val seasonalImageManager = SeasonalImageManager()
    }
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constants.SHARED_PREF_MAME, Context.MODE_PRIVATE)

    @SuppressLint("ApplySharedPref")
    fun saveClothesInSharedPreferences() {
        val autumn = seasonalImageManager.getImageList("autumn").joinToString(",")
        val summer = seasonalImageManager.getImageList("summer").joinToString(",")
        val winter = seasonalImageManager.getImageList("winter").joinToString(",")

        val editor = sharedPreferences.edit()
        editor.putString(Constants.AUTUMN, autumn)
        editor.putString(Constants.SUMMER, summer)
        editor.putString(Constants.WINTER, winter)
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
                key,
                imageIds.joinToString(",")
            )
            editor.apply()
        }
    }

    }


