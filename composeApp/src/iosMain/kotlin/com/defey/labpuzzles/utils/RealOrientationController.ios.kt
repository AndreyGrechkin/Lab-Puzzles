package com.defey.labpuzzles.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import com.defey.labpuzzles.OrientationController
import platform.Foundation.setValue
import platform.UIKit.UIDevice

@Composable
actual fun createOrientationController(): OrientationController {
    return IOSOrientationController()
}

class IOSOrientationController : OrientationController {

    @Composable
    override fun LockPortraitOrientation() {
        LaunchedEffect(Unit) {
            setScreenOrientation(false)
        }
        DisposableEffect(Unit) {
            onDispose {
                setScreenOrientation(true)
            }
        }
    }

    @Composable
    override fun LockLandscapeOrientation() {
        LaunchedEffect(Unit) {
            setScreenOrientation(false)
        }
        DisposableEffect(Unit) {
            onDispose {
                setScreenOrientation(true)
            }
        }
    }

    fun setScreenOrientation(isLandscape: Boolean) {
        val device = UIDevice.currentDevice
        val orientation = if (isLandscape) 4L else 1L
        device.setValue(orientation, forKey = "orientation")

    }
}