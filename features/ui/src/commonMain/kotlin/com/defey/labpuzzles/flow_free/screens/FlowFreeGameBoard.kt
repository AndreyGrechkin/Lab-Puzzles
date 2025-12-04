package com.defey.labpuzzles.flow_free.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.extensions.toComposeColor
import com.defey.labpuzzles.flow_free.FlowFreeUiContract
import com.defey.labpuzzles.models.ActiveLine
import com.defey.labpuzzles.models.FlowFreeCell
import com.defey.labpuzzles.models.FlowFreeError
import com.defey.labpuzzles.models.FlowFreePosition
import kotlin.math.abs
import kotlin.math.sin

@Composable
fun FlowFreeGameBoard(
    state: FlowFreeUiContract.State,
    onEvent: (FlowFreeUiContract.Event) -> Unit,
    modifier: Modifier = Modifier
) {
    val grid = state.grid
    val rows = grid.size
    val cols = if (rows > 0) grid[0].size else 0

    if (rows == 0 || cols == 0) return

    val pulseProgress by animateFloatAsState(
        targetValue = if (state.showError) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    BoxWithConstraints(
        modifier = modifier
    ) {
        // Расчет размера ячейки
        val cellSize = remember(constraints.maxWidth, constraints.maxHeight, cols) {
            val availableWidth = constraints.maxWidth.toFloat()
            val availableHeight = constraints.maxHeight.toFloat()
            val sizeBasedOnWidth = availableWidth / cols
            val sizeBasedOnHeight = availableHeight / rows
            minOf(sizeBasedOnWidth, sizeBasedOnHeight)
        }

        // Расчет смещения для центрирования сетки
        val totalWidth = cols * cellSize
        val totalHeight = rows * cellSize
        val offsetX = (constraints.maxWidth - totalWidth) / 2
        val offsetY = (constraints.maxHeight - totalHeight) / 2

        // Основной Canvas для отрисовки
        Canvas(modifier = Modifier.fillMaxSize()) {
            // 1. Рисуем статичную сетку и точки
            drawStaticBoard(
                grid = state.grid,
                offsetX = offsetX,
                offsetY = offsetY,
                cellSize = cellSize,
                rows = rows,
                cols = cols
            )

            // 2. Рисуем ЗАВЕРШЕННЫЕ ПУТИ (новый метод!)
            drawCompletedPaths(
                completedPaths = state.completedPaths,
                offsetX = offsetX,
                offsetY = offsetY,
                cellSize = cellSize
            )

            // 3. Рисуем активную линию (если есть)
            state.activeLine?.let { activeLine ->
                drawActiveLine(
                    activeLine = activeLine,
                    offsetX = offsetX,
                    offsetY = offsetY,
                    cellSize = cellSize
                )
            }

            // 4. Рисуем ошибку (если есть)
            if (state.showError && state.errorPosition != null) {
                drawError(
                    position = state.errorPosition,
                    errorType = state.errorType,
                    offsetX = offsetX,
                    offsetY = offsetY,
                    cellSize = cellSize,
                    pulseProgress = pulseProgress
                )
            }
        }

        // 5. Наложенный слой для жестов (невидимый)
        GestureOverlay(
            rows = rows,
            cols = cols,
            cellSize = cellSize,
            offsetX = offsetX,
            offsetY = offsetY,
            onEvent = onEvent,
            modifier = Modifier.matchParentSize()
        )
    }
}

/**
 * ОТРИСОВКА СТАТИЧНОЙ ЧАСТИ (сетка + точки)
 */
private fun DrawScope.drawStaticBoard(
    grid: List<List<FlowFreeCell>>,
    offsetX: Float,
    offsetY: Float,
    cellSize: Float,
    rows: Int,
    cols: Int
) {
    // 1. Сетка
    drawGrid(
        offsetX = offsetX,
        offsetY = offsetY,
        rows = rows,
        cols = cols,
        cellSize = cellSize
    )

    // 2. Точки (Endpoint)
    for (row in 0 until rows) {
        for (col in 0 until cols) {
            val cell = grid[row][col]
            if (cell is FlowFreeCell.Endpoint) {
                drawEndpoint(
                    position = FlowFreePosition(row, col),
                    color = cell.color.toComposeColor(),
                    offsetX = offsetX,
                    offsetY = offsetY,
                    cellSize = cellSize
                )
            }
        }
    }
}


/**
 * ОТРИСОВКА ЗАВЕРШЕННЫХ ПУТЕЙ (правильная реализация)
 */
private fun DrawScope.drawCompletedPaths(
    completedPaths: List<FlowFreeUiContract.CompletedPath>,
    offsetX: Float,
    offsetY: Float,
    cellSize: Float
) {

    for ((_, completedPath) in completedPaths.withIndex()) {
        val path = completedPath.positions
        val color = completedPath.color.toComposeColor()

        if (path.size < 2) continue

        // Рисуем линии между последовательными ячейками пути
        for (i in 0 until path.size - 1) {
            val from = path[i]
            val to = path[i + 1]

            // Проверяем что ячейки соседние (для безопасности)
            val rowDiff = abs(from.row - to.row)
            val colDiff = abs(from.col - to.col)
            val isAdjacent = (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1)

            if (isAdjacent) {
                drawLineBetweenCells(
                    from = from,
                    to = to,
                    color = color,
                    offsetX = offsetX,
                    offsetY = offsetY,
                    cellSize = cellSize,
                    strokeWidth = cellSize * 0.25f
                )
            }
        }
    }
}

/**
 * ОТРИСОВКА ЛИНИИ МЕЖДУ ЯЧЕЙКАМИ (общая функция)
 */
private fun DrawScope.drawLineBetweenCells(
    from: FlowFreePosition,
    to: FlowFreePosition,
    color: Color,
    offsetX: Float,
    offsetY: Float,
    cellSize: Float,
    strokeWidth: Float
) {
    val fromCenter = Offset(
        offsetX + from.col * cellSize + cellSize / 2,
        offsetY + from.row * cellSize + cellSize / 2
    )
    val toCenter = Offset(
        offsetX + to.col * cellSize + cellSize / 2,
        offsetY + to.row * cellSize + cellSize / 2
    )

    drawLine(
        color = color,
        start = fromCenter,
        end = toCenter,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )
}

/**
 * ОТРИСОВКА АКТИВНОЙ ЛИНИИ
 */
private fun DrawScope.drawActiveLine(
    activeLine: ActiveLine,
    offsetX: Float,
    offsetY: Float,
    cellSize: Float
) {
    if (activeLine.path.size < 2) return

    val lineColor = activeLine.color.toComposeColor().copy(alpha = 0.7f)

    drawLineForPositions(
        positions = activeLine.path,
        color = lineColor,
        offsetX = offsetX,
        offsetY = offsetY,
        cellSize = cellSize,
        strokeWidth = cellSize * 0.2f
    )

    // Подсветка последней точки активной линии
    val lastPosition = activeLine.path.last()
    drawCircle(
        color = lineColor,
        center = Offset(
            offsetX + lastPosition.col * cellSize + cellSize / 2,
            offsetY + lastPosition.row * cellSize + cellSize / 2
        ),
        radius = cellSize * 0.15f
    )
}

/**
 * ОТРИСОВКА ОШИБКИ
 */
private fun DrawScope.drawError(
    position: FlowFreePosition,
    errorType: FlowFreeError?,
    offsetX: Float,
    offsetY: Float,
    cellSize: Float,
    pulseProgress: Float = 1f
) {
    val alpha = 0.2f + 0.1f * sin(pulseProgress * 3.14).toFloat()
    val errorColor = when (errorType) {
        FlowFreeError.CELL_OCCUPIED -> Color(0xFFEF4444) // Красный
        FlowFreeError.NOT_AN_ENDPOINT -> Color(0xFFF59E0B) // Оранжевый
        FlowFreeError.NOT_ADJACENT_CELL -> Color(0xFF6B7280) // Серый
        FlowFreeError.INVALID_POSITION -> Color(0xFF9CA3AF) // Светло-серый
        else -> Color(0xFFDC2626) // Темно-красный
    }

    val cellRect = Rect(
        left = offsetX + position.col * cellSize,
        top = offsetY + position.row * cellSize,
        right = offsetX + position.col * cellSize + cellSize,
        bottom = offsetY + position.row * cellSize + cellSize
    )

    // 1. ФОН ЯЧЕЙКИ (полупрозрачная заливка)
    drawRect(
        color = errorColor.copy(alpha = alpha),
        topLeft = cellRect.topLeft,
        size = cellRect.size
    )

    // 2. РАМКА ЯЧЕЙКИ (толстая граница)
    drawRect(
        color = errorColor.copy(alpha = alpha),
        topLeft = cellRect.topLeft,
        size = cellRect.size,
        style = Stroke(width = 3.dp.toPx())
    )

    // 3. КРЕСТИК ОШИБКИ (опционально)
    if (errorType == FlowFreeError.CELL_OCCUPIED ||
        errorType == FlowFreeError.WRONG_COLOR_CONNECTION
    ) {

        val center = cellRect.center
        val crossSize = cellSize * 0.3f

        // Диагональный крестик (X)
        drawLine(
            color = errorColor.copy(alpha = alpha),
            start = Offset(center.x - crossSize, center.y - crossSize),
            end = Offset(center.x + crossSize, center.y + crossSize),
            strokeWidth = 2.dp.toPx()
        )

        drawLine(
            color = errorColor.copy(alpha = alpha),
            start = Offset(center.x + crossSize, center.y - crossSize),
            end = Offset(center.x - crossSize, center.y + crossSize),
            strokeWidth = 2.dp.toPx()
        )
    }

    // 4. ВОСКЛИЦАТЕЛЬНЫЙ ЗНАК (для NOT_AN_ENDPOINT)
    else if (errorType == FlowFreeError.NOT_AN_ENDPOINT) {
        val center = cellRect.center

        // Точка
        drawCircle(
            color = errorColor.copy(alpha = alpha),
            center = Offset(center.x, center.y + cellSize * 0.15f),
            radius = cellSize * 0.05f
        )

        // Палочка
        drawLine(
            color = errorColor.copy(alpha = alpha),
            start = Offset(center.x, center.y - cellSize * 0.15f),
            end = Offset(center.x, center.y + cellSize * 0.1f),
            strokeWidth = 2.dp.toPx()
        )
    }

    // 5. СТРЕЛКИ (для NOT_ADJACENT_CELL)
    else if (errorType == FlowFreeError.NOT_ADJACENT_CELL) {
        val center = cellRect.center

        // Стрелка вправо
        drawLine(
            color = errorColor.copy(alpha = alpha),
            start = Offset(center.x - cellSize * 0.2f, center.y),
            end = Offset(center.x + cellSize * 0.2f, center.y),
            strokeWidth = 2.dp.toPx()
        )

        // Наконечник стрелки
        drawLine(
            color = errorColor.copy(alpha = alpha),
            start = Offset(center.x + cellSize * 0.1f, center.y - cellSize * 0.1f),
            end = Offset(center.x + cellSize * 0.2f, center.y),
            strokeWidth = 2.dp.toPx()
        )

        drawLine(
            color = errorColor.copy(alpha = alpha),
            start = Offset(center.x + cellSize * 0.1f, center.y + cellSize * 0.1f),
            end = Offset(center.x + cellSize * 0.2f, center.y),
            strokeWidth = 2.dp.toPx()
        )
    }
}

// ============================================
// ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ ОТРИСОВКИ
// ============================================

private fun DrawScope.drawGrid(
    offsetX: Float,
    offsetY: Float,
    rows: Int,
    cols: Int,
    cellSize: Float
) {
    val gridColor = Color.White
    val strokeWidth = 1.dp.toPx()

    // Вертикальные линии
    for (col in 0..cols) {
        val x = offsetX + col * cellSize
        drawLine(
            color = gridColor,
            start = Offset(x, offsetY),
            end = Offset(x, offsetY + rows * cellSize),
            strokeWidth = strokeWidth
        )
    }

    // Горизонтальные линии
    for (row in 0..rows) {
        val y = offsetY + row * cellSize
        drawLine(
            color = gridColor,
            start = Offset(offsetX, y),
            end = Offset(offsetX + cols * cellSize, y),
            strokeWidth = strokeWidth
        )
    }
}

private fun DrawScope.drawEndpoint(
    position: FlowFreePosition,
    color: Color,
    offsetX: Float,
    offsetY: Float,
    cellSize: Float
) {
    val center = Offset(
        offsetX + position.col * cellSize + cellSize / 2,
        offsetY + position.row * cellSize + cellSize / 2
    )
    val radius = cellSize * 0.35f

    // Внутренний круг
    drawCircle(
        color = color,
        center = center,
        radius = radius
    )
}

private fun DrawScope.drawLineForPositions(
    positions: List<FlowFreePosition>,
    color: Color,
    offsetX: Float,
    offsetY: Float,
    cellSize: Float,
    strokeWidth: Float
) {
    for (i in 0 until positions.size - 1) {
        val from = positions[i]
        val to = positions[i + 1]

        // Проверяем что ячейки соседние
        val rowDiff = abs(from.row - to.row)
        val colDiff = abs(from.col - to.col)
        if ((rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1)) {
            val fromCenter = Offset(
                offsetX + from.col * cellSize + cellSize / 2,
                offsetY + from.row * cellSize + cellSize / 2
            )
            val toCenter = Offset(
                offsetX + to.col * cellSize + cellSize / 2,
                offsetY + to.row * cellSize + cellSize / 2
            )

            drawLine(
                color = color,
                start = fromCenter,
                end = toCenter,
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

// ============================================
// КОМПОНЕНТ ДЛЯ ЖЕСТОВ
// ============================================

@Composable
private fun GestureOverlay(
    rows: Int,
    cols: Int,
    cellSize: Float,
    offsetX: Float,
    offsetY: Float,
    onEvent: (FlowFreeUiContract.Event) -> Unit,
    modifier: Modifier = Modifier
) {
    // Локальное состояние для отслеживания последней ячейки
    var lastReportedCell by remember { mutableStateOf<FlowFreePosition?>(null) }

    Box(
        modifier = modifier
            .pointerInput(rows, cols, cellSize, offsetX, offsetY) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val position = offsetToCell(
                            offset = offset,
                            offsetX = offsetX,
                            offsetY = offsetY,
                            cellSize = cellSize,
                            rows = rows,
                            cols = cols
                        )
                        // Сбрасываем последнюю ячейку
                        lastReportedCell = position
                        onEvent(FlowFreeUiContract.Event.OnDragStart(position))
                    },
                    onDrag = { change, _ ->
                        change.consumeAllChanges()
                        val position = offsetToCell(
                            offset = change.position,
                            offsetX = offsetX,
                            offsetY = offsetY,
                            cellSize = cellSize,
                            rows = rows,
                            cols = cols
                        )
                        // Проверяем изменилась ли ячейка
                        if (position == lastReportedCell) {
                            // Та же ячейка - не отправляем событие
                            return@detectDragGestures
                        }

                        // Обновляем последнюю ячейку
                        lastReportedCell = position
                        onEvent(FlowFreeUiContract.Event.OnDragMove(position))
                    },
                    onDragEnd = {
                        // Сбрасываем последнюю ячейку
                        lastReportedCell = null
                        onEvent(FlowFreeUiContract.Event.OnDragEnd)
                    },
                    onDragCancel = {
                        // Сбрасываем последнюю ячейку
                        lastReportedCell = null
                        onEvent(FlowFreeUiContract.Event.OnDragCancel)
                    }
                )
            }
            .pointerInput(rows, cols, cellSize, offsetX, offsetY) {
                detectTapGestures(
                    onDoubleTap = { offset ->
                        // Можно использовать для быстрого удаления
                        val position = offsetToCell(
                            offset = offset,
                            offsetX = offsetX,
                            offsetY = offsetY,
                            cellSize = cellSize,
                            rows = rows,
                            cols = cols
                        )
                        onEvent(FlowFreeUiContract.Event.OnEndpointTap(position))
                    }
                )
            }
    )
}

/**
 * КОНВЕРТАЦИЯ КООРДИНАТ В ЯЧЕЙКУ
 */
private fun offsetToCell(
    offset: Offset,
    offsetX: Float,
    offsetY: Float,
    cellSize: Float,
    rows: Int,
    cols: Int
): FlowFreePosition {
    val relativeX = offset.x - offsetX
    val relativeY = offset.y - offsetY

    val col = (relativeX / cellSize).toInt()
    val row = (relativeY / cellSize).toInt()

    return FlowFreePosition(
        row = row.coerceIn(0, rows - 1),
        col = col.coerceIn(0, cols - 1)
    )
}