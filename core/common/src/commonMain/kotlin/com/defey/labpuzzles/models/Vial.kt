package com.defey.labpuzzles.models

import kotlinx.serialization.Serializable

@Serializable
data class Vial(
    val colors: List<Int>,
    val capacity: Int = 4
) {
    val isFull: Boolean get() = colors.size >= capacity
    val isEmpty: Boolean get() = colors.isEmpty()
    val topColor: Int? get() = colors.lastOrNull()
    val topColorCount: Int get() = colors.takeLastWhile { it == topColor }.size
}