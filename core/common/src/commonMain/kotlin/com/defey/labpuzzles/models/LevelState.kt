package com.defey.labpuzzles.models

import kotlinx.serialization.Serializable

@Serializable
data class LevelState(
    val isLocked: Boolean = true,
    val isCompleted: Boolean = false,
    val stars: Int = 0,
    val bestScore: Int = 0
) {
    val canPlay: Boolean get() = !isLocked
}