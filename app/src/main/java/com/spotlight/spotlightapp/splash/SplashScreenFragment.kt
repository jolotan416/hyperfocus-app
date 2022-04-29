package com.spotlight.spotlightapp.splash

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.spotlight.spotlightapp.R
import com.spotlight.spotlightapp.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {
    companion object {
        const val TAG = "SplashScreenFragment"
        const val REQUEST_KEY = "SplashScreenFragmentRequest"
        const val IS_FINISHED_SPLASH_ANIMATION = "is_finished_splash_animation"
    }

    private val splashScreenFragmentView: SplashScreenFragmentView by lazy {
        SplashScreenFragmentView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentSplashScreenBinding.bind(view).composeView.setContent {
            splashScreenFragmentView.CreateSplashScreen {
                parentFragmentManager.setFragmentResult(
                    REQUEST_KEY, bundleOf(IS_FINISHED_SPLASH_ANIMATION to true)
                )
            }
        }
    }
}