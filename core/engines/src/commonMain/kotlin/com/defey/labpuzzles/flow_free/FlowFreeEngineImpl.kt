package com.defey.labpuzzles.flow_free

import com.defey.labpuzzles.models.ErrorGameResult
import com.defey.labpuzzles.models.FlowFreeCell
import com.defey.labpuzzles.models.FlowFreeError
import com.defey.labpuzzles.models.FlowFreePosition
import com.defey.labpuzzles.models.FlowFreeState
import com.defey.labpuzzles.models.GameResult
import com.defey.labpuzzles.models.SuccessGameResult
import com.defey.labpuzzles.repository.FlowFreeEngine

/**
 * РЕАЛИЗАЦИЯ ДВИЖКА FLOW FREE
 *
 * 💡 ОСОБЕННОСТИ РЕАЛИЗАЦИИ:
 * - Линии проводятся пошагово между соседними ячейками
 * - Двойной тап на точку удаляет всю линию этого цвета
 * - Все ячейки должны быть заполнены для победы
 * - Линии не могут пересекаться или проходить через точки
 */
class FlowFreeEngineImpl : FlowFreeEngine {

    // ============================================
    // ОСНОВНОЙ МЕТОД - ВЫПОЛНЕНИЕ ХОДА
    // ============================================

    override fun makeMove(
        state: FlowFreeState,
        from: FlowFreePosition,
        to: FlowFreePosition
    ): GameResult<FlowFreeState> {
        // 1. Проверяем базовую валидность позиций
        val basicValidation = validateBasicMove(state, from, to)
        if (basicValidation != null) {
            return ErrorGameResult(state, basicValidation)
        }

        // 2. Проверяем что from - это либо точка, либо путь
        val fromCell = state.getCell(from)
        if (fromCell == null ||
            (fromCell !is FlowFreeCell.Endpoint && fromCell !is FlowFreeCell.Path)) {
            return ErrorGameResult(state, FlowFreeError.NOT_AN_ENDPOINT)
        }

        // 3. Проверяем что to - соседняя ячейка
        if (!arePositionsAdjacent(from, to)) {
            return ErrorGameResult(state, FlowFreeError.NOT_ADJACENT_CELL)
        }

        // 4. Получаем цвет начальной ячейки
        val fromColor = state.getCellColor(from)
        if (fromColor == null) {
            return ErrorGameResult(state, FlowFreeError.INVALID_POSITION)
        }

        // 5. Если from - это Path, проверяем что мы продолжаем линию, а не отходим от нее
        if (fromCell is FlowFreeCell.Path) {
            if (!isContinuingLine(state, from, to, fromColor)) {
                return ErrorGameResult(state, FlowFreeError.BREAKING_LINE)
            }
        }
        // Если from - это Endpoint, пропускаем эту проверку

        // 6. Проверяем целевую ячейку
        val toCell = state.getCell(to)
        val toValidation = validateTargetCell(toCell, fromColor)
        if (toValidation != null) {
            return ErrorGameResult(state, toValidation)
        }

        // 7. Создаем новое состояние с выполненным ходом
        val newGrid = applyMoveToGrid(state.grid, from, to, fromColor)
        val newState = state.copy(
            grid = newGrid,
            movesCount = state.movesCount + 1
        )

        // 8. Проверяем условие победы
        val isWin = checkWinCondition(newState)

        return SuccessGameResult(
            newState = newState.copy(isCompleted = isWin),
            isWin = isWin
        )
    }

    // ============================================
    // УДАЛЕНИЕ ЛИНИИ
    // ============================================

    override fun clearLine(
        state: FlowFreeState,
        color: Int
    ): GameResult<FlowFreeState> {
        // 1. Создаем копию сетки
        val newGrid = state.grid.map { row ->
            row.map { cell ->
                when (cell) {
                    is FlowFreeCell.Path -> {
                        // Удаляем пути этого цвета
                        if (cell.color == color) {
                            FlowFreeCell.Empty
                        } else {
                            cell
                        }
                    }
                    // Точки и пустые ячейки остаются без изменений
                    else -> cell
                }
            }
        }

        val newState = state.copy(grid = newGrid)

        return SuccessGameResult(
            newState = newState,
            isWin = checkWinCondition(newState)
        )
    }

    // ============================================
    // ПРОВЕРКА ПОБЕДЫ
    // ============================================

    override fun checkWinCondition(state: FlowFreeState): Boolean {
        // 1. Проверяем что все ячейки заполнены
        val allCellsFilled = state.grid.all { row ->
            row.all { cell ->
                cell !is FlowFreeCell.Empty
            }
        }

        if (!allCellsFilled) {
            return false
        }

        // 2. Находим все точки (Endpoints)
        val endpoints = mutableListOf<Pair<FlowFreePosition, Int>>()
        for (row in 0 until state.rows) {
            for (col in 0 until state.cols) {
                val cell = state.grid[row][col]
                if (cell is FlowFreeCell.Endpoint) {
                    endpoints.add(FlowFreePosition(row, col) to cell.color)
                }
            }
        }

        // 3. Группируем точки по цветам
        val endpointsByColor = endpoints.groupBy({ it.second }, { it.first })

        // 4. Для каждого цвета проверяем что его точки соединены
        return endpointsByColor.all { (color, colorEndpoints) ->
            // Должно быть ровно 2 точки каждого цвета
            if (colorEndpoints.size != 2) {
                return false
            }

            val (point1, point2) = colorEndpoints
            arePointsConnected(state, point1, point2, color)
        }
    }

    // ============================================
    // ПРОВЕРКА ВАЛИДНОСТИ ХОДА (для UI)
    // ============================================

    override fun isValidMove(
        state: FlowFreeState,
        from: FlowFreePosition,
        to: FlowFreePosition
    ): Boolean {
        // Используем ту же логику что и в makeMove, но без создания нового состояния
        return validateBasicMove(state, from, to) == FlowFreeError.INVALID_POSITION &&
                state.isEndpoint(from) &&
                arePositionsAdjacent(from, to) &&
                validateTargetCell(state.getCell(to), state.getCellColor(from) ?: -1) == FlowFreeError.INVALID_POSITION &&
                !isContinuingLine(state, from, to, state.getCellColor(from) ?: -1)
    }

    // ============================================
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // ============================================

    /**
     * БАЗОВАЯ ВАЛИДАЦИЯ ХОДА
     */
     private fun validateBasicMove(
        state: FlowFreeState,
        from: FlowFreePosition,
        to: FlowFreePosition
    ): FlowFreeError? { // Возвращаем null при успехе или ошибку
        // Проверяем что позиции в пределах поля
        if (!state.isValidPosition(from) || !state.isValidPosition(to)) {
            return FlowFreeError.INVALID_POSITION
        }

        // Проверяем что это не одна и та же ячейка
        if (from == to) {
            return FlowFreeError.INVALID_POSITION
        }

        return null // Успешная базовая валидация
    }

    /**
     * ПРОВЕРКА СОСЕДСТВА ЯЧЕЕК
     */
    fun arePositionsAdjacent(
        pos1: FlowFreePosition,
        pos2: FlowFreePosition
    ): Boolean {
        val rowDiff = kotlin.math.abs(pos1.row - pos2.row)
        val colDiff = kotlin.math.abs(pos1.col - pos2.col)

        // Соседние по вертикали или горизонтали (не по диагонали)
        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1)
    }

    /**
     * ПРОВЕРКА ЦЕЛЕВОЙ ЯЧЕЙКИ
     */
    private fun validateTargetCell(
        cell: FlowFreeCell?,
        fromColor: Int
    ): FlowFreeError? {
        return when (cell) {
            null -> FlowFreeError.INVALID_POSITION
            is FlowFreeCell.Endpoint -> {
                if (cell.color == fromColor) {
                    null // Успех
                } else {
                    // Точка другого цвета - ячейка занята
                    FlowFreeError.CELL_OCCUPIED
                }
            }
            is FlowFreeCell.Path -> {
                if (cell.color == fromColor) {
                    null // Успех
                } else {
                    FlowFreeError.CELL_OCCUPIED
                }
            }
            FlowFreeCell.Empty -> null // Успех
        }
    }

    /**
     * ПРИМЕНЕНИЕ ХОДА К СЕТКЕ
     */
    private fun applyMoveToGrid(
        grid: List<List<FlowFreeCell>>,
        from: FlowFreePosition,
        to: FlowFreePosition,
        color: Int
    ): List<List<FlowFreeCell>> {
        return grid.mapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, cell ->
                val currentPos = FlowFreePosition(rowIndex, colIndex)

                when {
                    // Целевая ячейка становится путем
                    currentPos == to -> {
                        val targetCell = grid[to.row][to.col]
                        when (targetCell) {
                            // Если это точка - оставляем точкой
                            is FlowFreeCell.Endpoint -> targetCell
                            // Иначе делаем путем
                            else -> FlowFreeCell.Path(color)
                        }
                    }

                    // Начальная ячейка (если это была точка - остается точкой)
                    currentPos == from -> {
                        cell // Оставляем как есть (Endpoint или Path)
                    }

                    // Все остальные ячейки без изменений
                    else -> cell
                }
            }
        }
    }

    /**
     * ПРОВЕРКА СОЕДИНЕНИЯ ТОЧЕК
     */
    private fun arePointsConnected(
        state: FlowFreeState,
        point1: FlowFreePosition,
        point2: FlowFreePosition,
        color: Int
    ): Boolean {
        val visited = mutableSetOf<FlowFreePosition>()
        val queue = ArrayDeque<FlowFreePosition>()

        queue.add(point1)
        visited.add(point1)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            // Если дошли до второй точки - соединены
            if (current == point2) {
                return true
            }

            val neighbors = listOf(
                FlowFreePosition(current.row - 1, current.col),
                FlowFreePosition(current.row + 1, current.col),
                FlowFreePosition(current.row, current.col - 1),
                FlowFreePosition(current.row, current.col + 1)
            )

            for (neighbor in neighbors) {
                if (state.isValidPosition(neighbor) &&
                    neighbor !in visited) {

                    val cell = state.getCell(neighbor)
                    val cellColor = state.getCellColor(neighbor)

                    // Можно переходить:
                    // 1. К целевой точке point2 (даже если это Endpoint)
                    // 2. К Path того же цвета
                    // 3. НЕ к другим Endpoint!

                    if (neighbor == point2 && cellColor == color) {
                        // Это целевая точка - можно идти
                        visited.add(neighbor)
                        queue.add(neighbor)
                    } else if (cell is FlowFreeCell.Path && cellColor == color) {
                        // Это путь того же цвета - можно идти
                        visited.add(neighbor)
                        queue.add(neighbor)
                    }
                    // Endpoint (кроме целевой) - нельзя проходить через них!
                }
            }
        }

        return false
    }

    /**
     * ПРОВЕРКА ЧТО МЫ ПРОДОЛЖАЕМ ЛИНИЮ, А НЕ ОТХОДИМ ОТ НЕЕ
     *
     * Когда from - это Path, мы можем ходить только:
     * 1. К пустой ячейке для продолжения линии
     * 2. К Endpoint того же цвета для завершения линии
     * 3. НЕ можем резко менять направление (создавать ответвления)
     */
    private fun isContinuingLine(
        state: FlowFreeState,
        from: FlowFreePosition,
        to: FlowFreePosition,
        color: Int
    ): Boolean {
        val sameColorNeighbors = getSameColorNeighbors(state, from, color)

        // Если у from только один сосед того же цвета (значит мы на конце линии),
        // то можем ходить в любом направлении (продолжать линию)
        if (sameColorNeighbors.size == 1) {
            return true
        }

        // Если у from два соседа того же цвета (значит мы в середине линии),
        // то to должен быть одним из этих соседей (продолжение в том же направлении)
        if (sameColorNeighbors.size == 2) {
            return sameColorNeighbors.any { it == to }
        }

        // Если больше 2 соседей - это пересечение, что запрещено
        return false
    }

    /**
     * ПОЛУЧЕНИЕ СОСЕДЕЙ ТОГО ЖЕ ЦВЕТА
     */
    private fun getSameColorNeighbors(
        state: FlowFreeState,
        position: FlowFreePosition,
        color: Int
    ): List<FlowFreePosition> {
        val neighbors = listOf(
            FlowFreePosition(position.row - 1, position.col), // вверх
            FlowFreePosition(position.row + 1, position.col), // вниз
            FlowFreePosition(position.row, position.col - 1), // влево
            FlowFreePosition(position.row, position.col + 1)  // вправо
        )

        return neighbors.filter { neighbor ->
            state.isValidPosition(neighbor) &&
                    state.getCellColor(neighbor) == color
        }
    }
}