package com.spotlight.spotlightapp.splash

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.spotlight.spotlightapp.R

class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {
    companion object {
        fun getInstance(splashScreenCallback: SplashScreenCallback) =
            SplashScreenFragment().apply {
                setCallback(splashScreenCallback)
            }
    }

    private lateinit var splashScreenCallback: SplashScreenCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ComposeView>(R.id.composeView).setContent {
            createSplashScreen()
        }
    }

    fun setCallback(splashScreenCallback: SplashScreenCallback) {
        this.splashScreenCallback = splashScreenCallback
    }

    @Composable
    private fun createSplashScreen() {
        // TODO: Configure views for splash screen
    }

    interface SplashScreenCallback {
        fun onFinishSplashScreenAnimation()
    }
}