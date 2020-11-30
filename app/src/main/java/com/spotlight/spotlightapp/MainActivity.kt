package com.spotlight.spotlightapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.spotlight.spotlightapp.splash.SplashScreenFragment

class MainActivity : AppCompatActivity(R.layout.activity_main), SplashScreenFragment.SplashScreenCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureSplashScreenFragment()
    }

    override fun onFinishSplashScreenAnimation() {
        // TODO: Replace splash screen fragment with daily intent list fragment
    }

    private fun configureSplashScreenFragment() {
        val splashScreenFragment = SplashScreenFragment.getInstance(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, splashScreenFragment)
            .commit()
    }
}
