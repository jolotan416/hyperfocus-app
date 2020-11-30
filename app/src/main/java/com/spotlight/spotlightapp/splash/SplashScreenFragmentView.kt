package com.spotlight.spotlightapp.splash

import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onActive
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.spotlight.spotlightapp.R

class SplashScreenFragmentView {
    companion object {
        private const val FADE_ANIMATION_DURATION = 1000
        private const val INITIAL_ALPHA = 1f
        private const val END_ALPHA = 0f
    }

    @Composable
    fun createSplashScreen(onAnimationFinish: () -> Unit) {
        val animatedProgress = animatedFloat(INITIAL_ALPHA)
        onActive {
            animatedProgress.animateTo(
                targetValue = END_ALPHA,
                anim = tween(FADE_ANIMATION_DURATION, easing = LinearEasing)
            )
        }

        val alpha = animatedProgress.value
        val splashScreenImage = imageResource(R.drawable.spotlight_logo)
        Image(
            asset = splashScreenImage,
            contentScale = ContentScale.Inside,
            modifier = Modifier.padding(60.dp),
            alpha = alpha
        )

        if (alpha == END_ALPHA) onAnimationFinish()
    }
}