package com.spotlight.spotlightapp.utilities

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.spotlight.spotlightapp.R

object TextConfiguration {
    val fontFamily: FontFamily
        get() = FontFamily(
            listOf(
                Font(R.font.lato_thin, weight = FontWeight.Thin),
                Font(R.font.lato_regular, weight = FontWeight.Normal),
                Font(R.font.lato_semibold, weight = FontWeight.SemiBold),
                Font(R.font.lato_medium, weight = FontWeight.Medium)))
}