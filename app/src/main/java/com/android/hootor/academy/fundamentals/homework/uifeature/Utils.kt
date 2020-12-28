package com.android.hootor.academy.fundamentals.homework.uifeature

import android.content.Context
import android.util.DisplayMetrics

object Utils {
    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        return (dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}