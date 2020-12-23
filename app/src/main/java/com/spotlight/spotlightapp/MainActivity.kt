package com.spotlight.spotlightapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.spotlight.spotlightapp.splash.SplashScreenFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureSplashScreenFragment()
    }

    private fun configureSplashScreenFragment() {
        supportFragmentManager.apply {
            setFragmentResultListener(
                SplashScreenFragment.REQUEST_KEY,
                this@MainActivity) { requestKey, result ->
                if (requestKey == SplashScreenFragment.REQUEST_KEY &&
                    result.getBoolean(SplashScreenFragment.IS_FINISHED_SPLASH_ANIMATION, false)) {
                    configureDailyIntentListFragment()
                }
            }
            commit {
                setReorderingAllowed(true)
                replace(R.id.fragmentContainer, SplashScreenFragment::class.java, null)
            }
        }
    }

    private fun configureDailyIntentListFragment() {
        // TODO: Add Daily Intent List Fragment
    }
}
