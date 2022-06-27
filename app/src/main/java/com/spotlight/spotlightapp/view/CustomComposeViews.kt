package com.spotlight.spotlightapp.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spotlight.spotlightapp.utilities.ComposeTextConfiguration

object CustomComposeViews {
    @Composable
    fun Button(
        modifier: Modifier, @DrawableRes imageRes: Int, labelText: String?,
        buttonColors: ButtonColors, onClickListener: () -> Unit) {
        Button(
            onClick = onClickListener,
            modifier = modifier,
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
            colors = buttonColors) {
            if (imageRes != 0) {
                Image(
                    painter = painterResource(id = imageRes), modifier = Modifier.size(16.dp),
                    contentDescription = labelText, colorFilter = ColorFilter.tint(
                        buttonColors.contentColor(enabled = true).value))
            }

            if (imageRes != 0 && !labelText.isNullOrBlank()) {
                Spacer(modifier = Modifier.width(8.dp))
            }

            if (!labelText.isNullOrBlank()) {
                Text(
                    text = labelText, fontFamily = ComposeTextConfiguration.fontFamily,
                    fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }
    }
}