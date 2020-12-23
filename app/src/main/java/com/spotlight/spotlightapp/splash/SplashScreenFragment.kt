package com.spotlight.spotlightapp.splash

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.spotlight.spotlightapp.R

class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {
    companion object {
        const val REQUEST_KEY = "SplashScreenFragmentRequest"
        const val IS_FINISHED_SPLASH_ANIMATION = "is_finished_splash_animation"
    }

    private val splashScreenFragmentView: SplashScreenFragmentView by lazy {
        SplashScreenFragmentView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ComposeView>(R.id.composeView).setContent {
            splashScreenFragmentView.createSplashScreen {
                parentFragmentManager.setFragmentResult(
                    REQUEST_KEY, bundleOf(IS_FINISHED_SPLASH_ANIMATION to true)
                )
            }
        }
    }
}