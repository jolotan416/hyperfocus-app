package com.spotlight.spotlightapp.utilities

import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @BindingAdapter("willShow")
    @JvmStatic
    fun changeVisibility(view: View, willShow: Boolean) {
        view.visibility = if (willShow) View.VISIBLE else View.GONE
    }
}