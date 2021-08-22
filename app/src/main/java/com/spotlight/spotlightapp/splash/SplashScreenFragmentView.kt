package com.spotlight.spotlightapp.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.spotlight.spotlightapp.R

class SplashScreenFragmentView {
    companion object {
        private const val FADE_ANIMATION_DURATION = 1000
        private const val INITIAL_ALPHA = 1f
        private const val END_ALPHA = 0f

        private const val VISIBILITY_TRANSITION_KEY = "visibilityTransition"
        private const val SPLASH_SCREEN_IMAGE_CONTENT_DESCRIPTION = "Splash Screen"
    }

    @Preview
    @Composable
    fun CreateSplashScreen(onAnimationFinish: () -> Unit) {
        val animatedAlpha = remember { Animatable(INITIAL_ALPHA) }
        LaunchedEffect(key1 = VISIBILITY_TRANSITION_KEY, block = {
            animatedAlpha.animateTo(
                targetValue = END_ALPHA,
                animationSpec = tween(FADE_ANIMATION_DURATION, easing = LinearEasing),
                block = { if (value == END_ALPHA) onAnimationFinish() })
        })

        val splashScreenImage = painterResource(R.drawable.spotlight_logo)
        Image(
            splashScreenImage,
            SPLASH_SCREEN_IMAGE_CONTENT_DESCRIPTION,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(60.dp),
            alpha = animatedAlpha.value
        )
    }
}