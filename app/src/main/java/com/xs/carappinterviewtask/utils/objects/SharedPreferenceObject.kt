package com.xs.carappinterviewtask.utils.objects

import android.content.Context

object SharedPreferenceObject {

    fun setFav(context: Context, carId: Int, isFav: Boolean) {
        val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean(carId.toString(), isFav).apply()
    }

    fun getFav(context: Context, carId: Int): Boolean {
        val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPref.getBoolean(carId.toString(), false)
    }

}