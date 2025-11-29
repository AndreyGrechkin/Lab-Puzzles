package com.defey.labpuzzles.water_sort

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

@Composable
fun WaterSortVial(
    colors: List<Color>,
    capacity: Int = 4,
    totalWidth: Dp,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    val totalWidth = totalWidth
    val totalHeight = totalWidth * 3
    if (colors.size > capacity) return

    Canvas(
        modifier = modifier
            .size(totalWidth, totalHeight)
    ) {
        val width = totalWidth.toPx()
        val height = totalHeight.toPx()

        // Параметры из описания:
        val neckOval1Height = (totalWidth / 4).toPx()  // Первый овал горлышка
        val neckOval2Height = (totalWidth.value / 2.5).dp.toPx()  // Второй овал горлышка
        val bodyHeight = height - neckOval2Height
        val stroke = 2.dp.toPx()

        // 1. ПЕРВЫЙ ОВАЛ ГОРЛЫШКА
        drawOval(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFB8E0F0).copy(alpha = 0.4f),
                    Color(0xFFE8F4F8).copy(alpha = 0.4f)
                )
            ),
            topLeft = Offset((totalWidth / 10).toPx(), 0f),
            size = Size((totalWidth * 0.83f).toPx(), neckOval1Height)
        )

        // 2. ВТОРОЙ ОВАЛ ГОРЛЫШКА
        drawOval(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFB8E0F0).copy(alpha = 0.4f),
                    Color(0xFFE8F4F8).copy(alpha = 0.4f)
                )
            ),
            topLeft = Offset(0f, 0f),
            size = Size(width, neckOval2Height)
        )

        // 3. ТЕЛО ПРОБИРКИ
        val bodyWidth = width * 0.7f
        val bodyLeft = (width - bodyWidth) / 2
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFB8E0F0).copy(alpha = 0.4f),
                    Color(0xFFE8F4F8).copy(alpha = 0.4f)
                )
            ),

            topLeft = Offset(bodyLeft, neckOval2Height - stroke / 2),
            size = Size(bodyWidth, height - bodyWidth / 2 - neckOval2Height)
        )

        // 4. НИЗ - ПОЛУОКРУЖНОСТЬ
        drawArc(
            color = Color(0xFFE8F4F8).copy(alpha = 0.4f),
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(bodyLeft, height - bodyWidth - stroke / 2),
            size = Size(bodyWidth, bodyWidth)
        )

        // 5. ОБВОДКА
        // Первый овал
        drawOval(
            color = if (isSelected) Color(0xFF00F5FF) else Color(0xFF4A90E2),
            topLeft = Offset((totalWidth / 10).toPx(), stroke / 2),
            size = Size((totalWidth * 0.83f).toPx(), neckOval1Height),
            style = Stroke(width = stroke)
        )

        // Второй овал
        drawOval(
            color = if (isSelected) Color(0xFF00F5FF) else Color(0xFF4A90E2),
            topLeft = Offset(stroke / 2, stroke / 2),
            size = Size(width - stroke, neckOval2Height - stroke),
            style = Stroke(width = stroke)
        )

        val offsetYOval =
            findYOnOval(bodyLeft, width / 2, neckOval2Height / 2, width, neckOval2Height)
        // Бока тела
        drawLine(
            color = if (isSelected) Color(0xFF00F5FF) else Color(0xFF4A90E2),
            start = Offset(bodyLeft, offsetYOval - stroke / 2),
            end = Offset(bodyLeft, height - offsetYOval - stroke / 2),
            strokeWidth = stroke
        )

        drawLine(
            color = if (isSelected) Color(0xFF00F5FF) else Color(0xFF4A90E2),
            start = Offset(bodyLeft + bodyWidth, offsetYOval - stroke / 2),
            end = Offset(bodyLeft + bodyWidth, height - offsetYOval - stroke / 2),
            strokeWidth = stroke
        )

        // Полуокружность
        drawArc(
            color = if (isSelected) Color(0xFF00F5FF) else Color(0xFF4A90E2),
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(bodyLeft, height - bodyWidth - stroke / 2),
            size = Size(bodyWidth, bodyWidth),
            style = Stroke(width = stroke)
        )

        // 6. ЖИДКОСТЬ (упрощенная версия - без сложных расчетов)
        if (colors.isNotEmpty()) {
            val liquidAreaBottom = height - stroke
            val liquidAreaHeight =
                liquidAreaBottom - neckOval2Height - 2.dp.toPx() // остступ что ы всклей не было
            val segmentHeight = liquidAreaHeight / capacity
            val arcBeginPosition = height - bodyWidth / 2

            colors.forEachIndexed { index, color ->
                val liquidColor = color.copy(alpha = 0.9f)
                val yPosition = liquidAreaBottom - (segmentHeight * (index + 1))
                when {
                    segmentHeight <= bodyWidth / 2 && yPosition > arcBeginPosition -> {
                        val clipPath = Path().apply {
                            val right = bodyLeft + bodyWidth
                            addRect(
                                Rect(
                                    Offset(bodyLeft, yPosition),
                                    Offset(right, yPosition + segmentHeight)
                                )
                            )
                        }

                        // 2. Рисуем полный круг, но обрезаем по сегменту
                        withTransform({
                            clipPath(clipPath, ClipOp.Intersect)
                        }) {
                            drawCircle(
                                color = color,
                                center = Offset(
                                    bodyLeft + bodyWidth / 2,
                                    height - bodyWidth / 2 - stroke / 2,
                                ),
                                radius = bodyWidth / 2 - stroke / 2
                            )
                        }
                    }

                    arcBeginPosition in yPosition..(yPosition + segmentHeight) -> {
                        drawRect(
                            color = liquidColor,
                            topLeft = Offset(bodyLeft + stroke / 2, yPosition),
                            size = Size(bodyWidth - stroke, arcBeginPosition - yPosition)
                        )
                        val clipPath = Path().apply {
                            val right = bodyLeft + bodyWidth
                            addRect(
                                Rect(
                                    Offset(bodyLeft, arcBeginPosition - 0.3f),
                                    Offset(right, yPosition + segmentHeight)
                                )
                            )
                        }

                        // 2. Рисуем полный круг, но обрезаем по сегменту
                        withTransform({
                            clipPath(clipPath, ClipOp.Intersect)
                        }) {
                            drawCircle(
                                color = color,
                                center = Offset(
                                    bodyLeft + bodyWidth / 2,
                                    height - bodyWidth / 2 - stroke / 2,
                                ),
                                radius = bodyWidth / 2 - stroke / 2
                            )
                        }
                    }

                    else -> {
                        // Основная часть - прямой прямоугольник
                        drawRect(
                            color = liquidColor,
                            topLeft = Offset(bodyLeft + stroke / 2, yPosition),
                            size = Size(bodyWidth - stroke, segmentHeight)
                        )
                    }
                }
            }
        }

        // 7. БЛИКИ
        val highlightWidth = width * 0.3f
        val highlightLeft = width * 0.1f

        // Блик на горлышке
        drawOval(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0f),
                    Color.White.copy(alpha = 0.3f),
                    Color.White.copy(alpha = 0f)
                )
            ),
            topLeft = Offset(highlightLeft, height * 0.05f),
            size = Size(highlightWidth, neckOval1Height * 0.3f)
        )

        // Блик на теле
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0f),
                    Color.White.copy(alpha = 0.2f),
                    Color.White.copy(alpha = 0f)
                )
            ),
            topLeft = Offset(highlightLeft, neckOval1Height + neckOval2Height + height * 0.1f),
            size = Size(highlightWidth, bodyHeight * 0.6f)
        )
    }
}

private fun findYOnOval(
    x: Float,
    centerX: Float,
    centerY: Float,
    ovalWidth: Float,
    ovalHeight: Float
): Float {
    val a = ovalWidth / 2
    val b = ovalHeight / 2
    val relativeX = x - centerX

    val yRelative = b * sqrt(1f - (relativeX * relativeX) / (a * a))
    return centerY + yRelative
}