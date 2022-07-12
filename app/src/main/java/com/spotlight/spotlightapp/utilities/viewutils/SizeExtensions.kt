package com.spotlight.spotlightapp.utilities.viewutils

import android.content.res.Resources
import android.util.TypedValue

fun Float.dpToPx(resources: Resources): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)
        .toInt()
}