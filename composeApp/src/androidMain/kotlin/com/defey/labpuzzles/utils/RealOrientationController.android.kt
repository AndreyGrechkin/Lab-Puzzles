package com.defey.labpuzzles.utils

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import com.defey.labpuzzles.OrientationController

@Composable
actual fun createOrientationController(): OrientationController {
    return AndroidOrientationController()
}

class AndroidOrientationController : OrientationController {

    @Composable
    override fun LockPortraitOrientation() {
        LockScreenOrientation(ScreenOrientation.PORTRAIT)
        UnlockScreenOrientationOnDispose()
    }

    @Composable
    override fun LockLandscapeOrientation() {
        LockScreenOrientation(ScreenOrientation.LANDSCAPE)
        UnlockScreenOrientationOnDispose()
    }

    @SuppressLint("ContextCastToActivity")
    @Composable
    private fun LockScreenOrientation(orientation: ScreenOrientation) {
        val activity = LocalActivity.current

        LaunchedEffect(orientation) {
            activity?.requestedOrientation = when (orientation) {
                ScreenOrientation.PORTRAIT -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                ScreenOrientation.LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }
    }

    @SuppressLint("ContextCastToActivity")
    @Composable
    private fun UnlockScreenOrientationOnDispose() {
        val activity = LocalActivity.current

        DisposableEffect(Unit) {
            onDispose {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }
    }
}