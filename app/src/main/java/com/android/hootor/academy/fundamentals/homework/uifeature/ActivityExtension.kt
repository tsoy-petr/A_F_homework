package com.android.hootor.academy.fundamentals.homework

import android.app.Activity
import android.graphics.Color
import android.view.WindowManager

fun Activity.makeStatusBarTransparent() {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    window.statusBarColor = Color.TRANSPARENT
}
