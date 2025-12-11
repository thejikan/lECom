package com.example.lecom.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.blur

@Composable
fun GlassContainer(
    modifier: Modifier = Modifier,
    width: Dp = Dp.Infinity,
    height: Dp = Dp.Infinity,
    borderRadius: Dp = 20.dp,
    blur: Dp = 10.dp,
    opacity: Float = 0.2f,
    borderColor: Color = Color.White,
    borderWidth: Dp = 1.5.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .then(
                if (width != Dp.Infinity) Modifier.width(width) else Modifier.fillMaxWidth()
            )
            .then(
                if (height != Dp.Infinity) Modifier.height(height) else Modifier.fillMaxHeight()
            )
            .clip(RoundedCornerShape(borderRadius))
            .blur(blur)
            .background(Color.White.copy(alpha = opacity))
            .border(
                width = borderWidth,
                color = borderColor.copy(alpha = 0.3f),
                shape = RoundedCornerShape(borderRadius)
            )
    ) {
        content()
    }
}
