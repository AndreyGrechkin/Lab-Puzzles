package com.defey.labpuzzles.utils

import androidx.compose.runtime.Composable
import com.defey.labpuzzles.OrientationController

@Composable
expect fun createOrientationController(): OrientationController

internal enum class ScreenOrientation {
    PORTRAIT, LANDSCAPE
}