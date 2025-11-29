package com.defey.labpuzzles.water_sort

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.water_sort.calculations.calculateNeckPosition
import com.defey.labpuzzles.water_sort.calculations.calculateTargetNeckPosition

@Composable
fun DrawPouringStream(
    sourcePosition: Position,
    targetPosition: Position,
    color: Color,
    progress: Float,
    density: Density,
    direction: PourDirection
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val sourceNeck = calculateNeckPosition(sourcePosition, direction)
        val targetNeck = calculateTargetNeckPosition(targetPosition, density)
        val streamWidth = with(density) { 12.dp.toPx() }

        drawLine(
            color = color.copy(alpha = 0.7f),
            start = sourceNeck,
            end = targetNeck,
            strokeWidth = streamWidth,
            cap = StrokeCap.Round
        )

        if (progress > 0f) {
            val totalLength = targetNeck.y - sourceNeck.y
            val currentLength = totalLength * progress

            val flowEnd = sourceNeck + Offset(0f, currentLength)

            drawLine(
                color = color,
                start = sourceNeck,
                end = flowEnd,
                strokeWidth = streamWidth * 0.8f,
                cap = StrokeCap.Round
            )

            if (progress > 0.7f) {
                val dropProgress = (progress - 0.7f) / 0.3f
                val dropY = sourceNeck.y + totalLength * dropProgress

                drawCircle(
                    color = color,
                    center = Offset(targetNeck.x, dropY),
                    radius = streamWidth / 2
                )
            }
        }
    }
}