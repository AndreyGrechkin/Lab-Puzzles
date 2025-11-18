package com.defey.labpuzzles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

interface OrientationController {
    @Composable
    fun LockPortraitOrientation()

    @Composable
    fun LockLandscapeOrientation()
}

val LocalOrientationController = staticCompositionLocalOf<OrientationController> {
    error("OrientationController not provided")
}