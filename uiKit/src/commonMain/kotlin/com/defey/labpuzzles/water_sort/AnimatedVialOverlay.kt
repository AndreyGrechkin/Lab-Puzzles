package com.defey.labpuzzles.water_sort

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.defey.labpuzzles.extensions.toComposeColor
import com.defey.labpuzzles.models.Vial
import com.defey.labpuzzles.water_sort.calculations.calculateDeselectPosition
import com.defey.labpuzzles.water_sort.calculations.calculateDeselectTransform
import com.defey.labpuzzles.water_sort.calculations.calculateMovePosition
import com.defey.labpuzzles.water_sort.calculations.calculateMoveTransform
import com.defey.labpuzzles.water_sort.calculations.calculatePourPosition
import com.defey.labpuzzles.water_sort.calculations.calculatePourTransform
import com.defey.labpuzzles.water_sort.calculations.calculateReturnPosition
import com.defey.labpuzzles.water_sort.calculations.calculateReturnTransform
import com.defey.labpuzzles.water_sort.calculations.calculateSelectPosition
import com.defey.labpuzzles.water_sort.calculations.calculateSelectTransform
import com.defey.labpuzzles.water_sort.calculations.calculateShakePosition
import com.defey.labpuzzles.water_sort.calculations.calculateShakeTransform

@Composable
fun AnimatedVialOverlay(
    vial: Vial,
    animationState: PourAnimationState,
    vialPositions: Map<Int, Position>,
    totalWidth: Dp,
    sourcePosition: Position,
    density: Density,
    onClick: () -> Unit
) {

    val (currentPosition, transform) = when (animationState) {
        // АНИМАЦИЯ ВЫБОРА - поднятие
        is PourAnimationState.SelectingVial -> {
            calculateSelectPosition(sourcePosition, animationState.progress) to
                    calculateSelectTransform(animationState.progress, density)
        }

        // СОСТОЯНИЕ ПОДНЯТОЙ ПРОБИРКИ (после анимации)
        is PourAnimationState.VialLifted -> {
            calculateSelectPosition(sourcePosition, 1f) to
                    calculateSelectTransform(1f, density)
        }

        // АНИМАЦИЯ ОТМЕНЫ ВЫБОРА - опускание
        is PourAnimationState.DeselectingVial -> {
            calculateDeselectPosition(sourcePosition, animationState.progress) to
                    calculateDeselectTransform(animationState.progress, density)
        }

        // АНИМАЦИЯ НЕВАЛИДНОГО ХОДА - покачивание
        is PourAnimationState.InvalidMoveShake -> {
            calculateShakePosition(sourcePosition, animationState.progress) to
                    calculateShakeTransform(density)
        }

        // ФАЗА ДВИЖЕНИЯ К ЦЕЛИ (из поднятого состояния)
        is PourAnimationState.MovingToTarget -> {
            val targetPos = vialPositions[animationState.targetIndex] ?: sourcePosition
            calculateMovePosition(
                sourcePosition, // УЖЕ ПОДНЯТАЯ ПОЗИЦИЯ
                targetPos,
                animationState.progress,
                animationState.direction,
            ) to
                    calculateMoveTransform(animationState.progress, animationState.direction)
        }

        // ФАЗА ПЕРЕЛИВАНИЯ (СТРУЯ)
        is PourAnimationState.PouringStream -> {
            val targetPos = vialPositions[animationState.targetIndex] ?: sourcePosition
            calculatePourPosition(sourcePosition, targetPos, animationState.direction) to
                    calculatePourTransform(animationState.direction)
        }

        // ФАЗА ВОЗВРАТА НА МЕСТО
        is PourAnimationState.ReturningBack -> {
            val targetPos = vialPositions[animationState.targetIndex] ?: sourcePosition
            calculateReturnPosition(
                sourcePosition,
                targetPos,
                animationState.progress,
                animationState.direction
            ) to
                    calculateReturnTransform(animationState.progress, animationState.direction)
        }

        // Остальные случаи (переливание) - будут в следующих этапах
        else -> sourcePosition to VialTransform()
    }

    // Отрисовываем анимированную пробирку
    Box(
        modifier = Modifier

            .offset(
                x = with(density) { currentPosition.x.toDp() },
                y = with(density) { currentPosition.y.toDp() }
            )
            .size(
                width = with(density) { currentPosition.width.toDp() },
                height = with(density) { currentPosition.height.toDp() }
            )
            .graphicsLayer {
                rotationZ = transform.rotation
                scaleX = transform.scale
                scaleY = transform.scale
                shadowElevation = transform.elevation
                transformOrigin = transform.transformOrigin
            }
            .clickable(onClick = onClick)
    ) {
        WaterSortVial(
            colorsInt = vial.colors,
            capacity = vial.capacity,
            totalWidth = totalWidth,
            isSelected = true
        )
    }

    // ОТРИСОВЫВАЕМ СТРУЮ ЕСЛИ НУЖНО
    if (animationState is PourAnimationState.PouringStream) {
        val targetPos = vialPositions[animationState.targetIndex] ?: return
        DrawPouringStream(
            sourcePosition = currentPosition,
            targetPosition = targetPos,
            color = animationState.color.toComposeColor(),
            progress = animationState.progress,
            density = density,
            direction = animationState.direction
        )
    }
}