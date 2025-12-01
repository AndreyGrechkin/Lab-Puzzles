package com.defey.labpuzzles.water_sort

import androidx.compose.ui.graphics.Color

sealed class PourAnimationState {
    object Idle : PourAnimationState()                          // Без анимации
    data class SelectingVial(val selectedIndex: Int, val progress: Float) :
        PourAnimationState() // Выбор пробирки

    data class DeselectingVial(val selectedIndex: Int, val progress: Float) :
        PourAnimationState() // Отмена выбора

    data class InvalidMoveShake(val selectedIndex: Int, val progress: Float) :
        PourAnimationState() // Невалидный ход

    data class VialLifted(val selectedIndex: Int) : PourAnimationState()

    // Анимации переливания - ТЕПЕРЬ С НАПРАВЛЕНИЕМ

    data class MovingToTarget(
        val sourceIndex: Int,
        val targetIndex: Int,
        val direction: PourDirection,
        val progress: Float
    ) : PourAnimationState()

    data class PouringStream(
        val sourceIndex: Int,
        val targetIndex: Int,
        val direction: PourDirection,
        val color: Int,
        val progress: Float
    ) : PourAnimationState()

    data class ReturningBack(
        val sourceIndex: Int,
        val targetIndex: Int,
        val direction: PourDirection,
        val progress: Float
    ) : PourAnimationState()
}