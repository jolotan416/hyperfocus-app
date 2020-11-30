package com.spotlight.spotlightapp.splash

import android.os.Bundle
import android.view.View
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

    private val splashScreenFragmentView: SplashScreenFragmentView by lazy {
        SplashScreenFragmentView()
    }

    private lateinit var splashScreenCallback: SplashScreenCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ComposeView>(R.id.composeView).setContent {
            splashScreenFragmentView.createSplashScreen {
                splashScreenCallback.onFinishSplashScreenAnimation()
            }
        }
    }

    fun setCallback(splashScreenCallback: SplashScreenCallback) {
        this.splashScreenCallback = splashScreenCallback
    }

    interface SplashScreenCallback {
        fun onFinishSplashScreenAnimation()
    }
}