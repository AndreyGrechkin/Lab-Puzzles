package com.defey.labpuzzles.water_sort

import androidx.compose.ui.graphics.TransformOrigin

data class VialTransform(
    val rotation: Float = 0f,
    val scale: Float = 1f,
    val elevation: Float = 0f,
    val transformOrigin: TransformOrigin = TransformOrigin.Center
)