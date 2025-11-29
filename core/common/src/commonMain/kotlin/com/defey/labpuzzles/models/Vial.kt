package com.defey.labpuzzles.models

import androidx.compose.ui.graphics.Color

data class Vial(
    val colors: List<Color>,
    val capacity: Int = 4
) {
    val isFull: Boolean get() = colors.size >= capacity
    val isEmpty: Boolean get() = colors.isEmpty()
    val topColor: Color? get() = colors.lastOrNull()
    val topColorCount: Int get() = colors.takeLastWhile { it == topColor }.size
}