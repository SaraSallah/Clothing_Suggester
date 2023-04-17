package com.example.clothing_suggester

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.clothing_suggester.util.Constant

class ClothesManager(context : Context) {
    private val sharedPreferences :SharedPreferences =
        context.getSharedPreferences(Constant.SHARED_PREF_MAME,Context.MODE_PRIVATE)
    private val summerList = listOf<Int>(
        R.drawable.sum1,
        R.drawable.sum2,
        R.drawable.sum3,
        // Add more image resource IDs as needed
    )
    fun saveClothesInSharedPreferenc(list : List<Int>) {
        val summerListID = list.joinToString(",") // Use a comma as delimiter
        val editor = sharedPreferences.edit()
        editor.putString(Constant.KEY, summerListID)
        editor.commit()
    }

    fun getImageIds(): List<Int> {
        val imageIdsString = sharedPreferences.getString(Constant.KEY, "")
        return if (imageIdsString.isNullOrEmpty()) {
            emptyList()
        } else {
            imageIdsString.split(",").map { it.toInt() }
        }
    }
        // Function to get a random image resource ID from the shuffled array
        fun getRandomImageResourceId(): Int? {
            val imageIds = getImageIds()
            if (imageIds.isEmpty()) {
                return null
            }
            val shuffledIds = imageIds.shuffled()
            return shuffledIds.firstOrNull()
        }

        // Function to remove the used image resource ID from the shuffled array
        fun removeUsedImageResourceId() {
            val imageIds = getImageIds().toMutableList() // Convert to mutable list
            if (imageIds.isNotEmpty()) {
                imageIds.removeAt(0) // Remove the first element, which represents the used image resource ID
                val editor = sharedPreferences.edit()
                editor.putString(Constant.KEY, imageIds.joinToString(",")) // Convert back to comma-separated string
                editor.apply() // Use apply() instead of commit() for asynchronous saving
            }
        }

    }


