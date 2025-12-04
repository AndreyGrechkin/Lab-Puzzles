package com.defey.labpuzzles.flow_free

import com.defey.labpuzzles.models.ErrorGameResult
import com.defey.labpuzzles.models.FlowFreeCell
import com.defey.labpuzzles.models.FlowFreeError
import com.defey.labpuzzles.models.FlowFreePosition
import com.defey.labpuzzles.models.FlowFreeState
import com.defey.labpuzzles.models.GameResult
import com.defey.labpuzzles.models.LineContinueResult
import com.defey.labpuzzles.models.LineStartResult
import com.defey.labpuzzles.models.SuccessGameResult
import com.defey.labpuzzles.repository.FlowFreeEngine
import kotlin.math.abs

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

    override fun startNewLine(
        state: FlowFreeState,
        startPosition: FlowFreePosition
    ): LineStartResult {

        // 1. БАЗОВАЯ ВАЛИДАЦИЯ ПОЗИЦИИ
        val positionError = validateBasicPosition(state, startPosition)
        if (positionError != null) {
            return LineStartResult.Error(positionError, startPosition)
        }

        // 2. ПОЛУЧАЕМ ЯЧЕЙКУ
        val startCell = getCell(state, startPosition)
            ?: return LineStartResult.Error(FlowFreeError.INVALID_POSITION, startPosition)

        // 3. ПРОВЕРЯЕМ ЧТО ЭТО ENDPOINT
        if (startCell !is FlowFreeCell.Endpoint) {
            return LineStartResult.Error(FlowFreeError.NOT_AN_ENDPOINT, startPosition)
        }

        val lineColor = startCell.color

        // 4. ПРОВЕРЯЕМ ЧТО ДЛЯ ЭТОГО ЦВЕТА ЕЩЕ НЕТ ЛИНИИ
        if (hasLineForColor(state, lineColor)) {
            return LineStartResult.Error(FlowFreeError.CELL_OCCUPIED, startPosition)
        }

        // 5. ВСЕ ПРОВЕРКИ ПРОЙДЕНЫ - ВОЗВРАЩАЕМ УСПЕХ
        return LineStartResult.Success(
            lineColor = lineColor,
            startPosition = startPosition
        )
    }

    override fun continueLine(
        state: FlowFreeState,
        currentPath: List<FlowFreePosition>,
        newPosition: FlowFreePosition
    ): LineContinueResult {
        // 1. ВАЛИДАЦИЯ ВХОДНЫХ ДАННЫХ
        if (currentPath.isEmpty()) {
            return LineContinueResult.Error(
                error = FlowFreeError.INVALID_POSITION,
                position = newPosition
            )
        }

        // 2. БАЗОВАЯ ВАЛИДАЦИЯ НОВОЙ ПОЗИЦИИ
        val positionError = validateBasicPosition(state, newPosition)
        if (positionError != null) {
            return LineContinueResult.Error(positionError, newPosition)
        }

        // 3. ПОЛУЧАЕМ ЦВЕТ ЛИНИИ ИЗ ПЕРВОЙ ТОЧКИ
        val lineColor = getPathColor(state, currentPath) ?: return LineContinueResult.Error(
            error = FlowFreeError.INVALID_POSITION,
            position = newPosition
        )

        // 4. ПРОВЕРЯЕМ ЧТО НОВАЯ ПОЗИЦИЯ ЕЩЕ НЕ В ПУТИ (кроме backtrack)
        if (currentPath.contains(newPosition)) {
            // Это backtrack - допустимо, но нужно специальная обработка
            return handleBacktrack(currentPath, newPosition)
        }

        // 5. ПОЛУЧАЕМ ПОСЛЕДНЮЮ ПОЗИЦИЮ ПУТИ
        val lastPosition = currentPath.last()

        // 6. ПРОВЕРЯЕМ СОСЕДСТВО С ПОСЛЕДНЕЙ ПОЗИЦИЕЙ
        if (!arePositionsAdjacent(lastPosition, newPosition)) {
            return LineContinueResult.Error(
                error = FlowFreeError.NOT_ADJACENT_CELL,
                position = newPosition
            )
        }

        // 7. ПРОВЕРЯЕМ ЯЧЕЙКУ (цвет, занятость)
        val cellValidation = validateCellForLine(
            state = state,
            position = newPosition,
            lineColor = lineColor
        )

        if (cellValidation != null) {
            return LineContinueResult.Error(cellValidation, newPosition)
        }

        // 8. ПРОВЕРЯЕМ ПРАВИЛА ЛИНИЙ (не пересечение, не разрыв)
        val lineValidation = validateLineRules(
            state = state,
            currentPath = currentPath,
            newPosition = newPosition,
            lineColor = lineColor
        )

        if (lineValidation != null) {
            return LineContinueResult.Error(lineValidation, newPosition)
        }

        // 9. ФОРМИРУЕМ НОВЫЙ ПУТЬ
        val newPath = currentPath + newPosition

        // 10. ПРОВЕРЯЕМ ДОСТИГЛИ ЛИ ВТОРОЙ ТОЧКИ
        val reachedEndpoint = checkIfReachedEndpoint(
            state = state,
            position = newPosition,
            lineColor = lineColor
        )

        // 11. ВОЗВРАЩАЕМ УСПЕШНЫЙ РЕЗУЛЬТАТ
        return LineContinueResult.Success(
            newPath = newPath,
            reachedEndpoint = reachedEndpoint
        )
    }

    override fun completeLine(
        state: FlowFreeState,
        path: List<FlowFreePosition>
    ): GameResult<FlowFreeState> {
        // 1. ВАЛИДАЦИЯ ПУТИ
        if (path.size < 2) {
            return ErrorGameResult(
                newState = state,
                error = FlowFreeError.INVALID_POSITION
            )
        }

        // 2. ПОЛУЧАЕМ ЦВЕТ ЛИНИИ ИЗ ПЕРВОЙ ТОЧКИ
        val lineColor = getPathColor(state, path) ?: return ErrorGameResult(
            newState = state,
            error = FlowFreeError.INVALID_POSITION
        )

        // 3. ПРОВЕРЯЕМ ЧТО ПЕРВАЯ ТОЧКА - ENDPOINT
        val startCell = getCell(state, path.first())
        if (startCell !is FlowFreeCell.Endpoint) {
            return ErrorGameResult(
                newState = state,
                error = FlowFreeError.NOT_AN_ENDPOINT
            )
        }

        // 4. ПРОВЕРЯЕМ ЧТО ПОСЛЕДНЯЯ ТОЧКА - ENDPOINT
        val endCell = getCell(state, path.last())
        if (endCell !is FlowFreeCell.Endpoint) {
            return ErrorGameResult(
                newState = state,
                error = FlowFreeError.NOT_AN_ENDPOINT
            )
        }

        // 5. ПРОВЕРЯЕМ ЧТО ОБЕ ТОЧКИ ОДНОГО ЦВЕТА
        if (startCell.color != endCell.color || startCell.color != lineColor) {
            return ErrorGameResult(
                newState = state,
                error = FlowFreeError.WRONG_COLOR_CONNECTION
            )
        }

        // 6. ПРОВЕРЯЕМ ЧТО ДЛЯ ЭТОГО ЦВЕТА ЕЩЕ НЕТ ЛИНИИ
        if (hasLineForColor(state, lineColor)) {
            return ErrorGameResult(
                newState = state,
                error = FlowFreeError.CELL_OCCUPIED
            )
        }

        // 7. ПОШАГОВАЯ ПРОВЕРКА ВСЕГО ПУТИ
        val pathValidation = validateCompletePath(state, path, lineColor)
        if (pathValidation != null) {
            return ErrorGameResult(
                newState = state,
                error = pathValidation
            )
        }

        // 8. ПРИМЕНЯЕМ ПУТЬ К GRID
        val newGrid = applyPathToGrid(state.grid, path, lineColor)

        val tempState = state.copy(
            grid = newGrid,
            movesCount = state.movesCount + 1,
            activeLine = null  // очищаем активную линию
        )

        // 9. ВЫЧИСЛЯЕМ ПРОГРЕСС
        val newProgress = calculateProgress(tempState)

        // 10. СОЗДАЕМ ФИНАЛЬНОЕ СОСТОЯНИЕ
        val newState = tempState.copy(
            progress = newProgress
        )

        // 10. ПРОВЕРЯЕМ УСЛОВИЕ ПОБЕДЫ
        val isWin = checkWinCondition(newState)

        // 11. ВОЗВРАЩАЕМ РЕЗУЛЬТАТ
        return SuccessGameResult(
            newState = newState.copy(isCompleted = isWin),
            isWin = isWin
        )
    }

    override fun removeLine(
        state: FlowFreeState,
        color: Int
    ): GameResult<FlowFreeState> {
        // 1. ПРОВЕРЯЕМ ЧТО ЦВЕТ ВАЛИДЕН
        if (color == -1) {
            return ErrorGameResult(
                newState = state,
                error = FlowFreeError.INVALID_POSITION
            )
        }

        // 2. ПРОВЕРЯЕМ ЧТО ДЛЯ ЭТОГО ЦВЕТА ЕСТЬ ЛИНИЯ
        if (!hasLineForColor(state, color)) {
            // Возвращаем успех с тем же состоянием (ничего не удаляем)
            return SuccessGameResult(
                newState = state,
                isWin = false
            )
        }

        // 3. ПОЛУЧАЕМ ENDPOINT ЭТОГО ЦВЕТА (чтобы не удалить их)
        val endpoints = getEndpointsByColor(state)[color] ?: emptyList()

        // 4. СОЗДАЕМ НОВЫЙ GRID БЕЗ PATH ЭТОГО ЦВЕТА
        val newGrid = removeColorFromGrid(state.grid, color, endpoints)

        // 4. СОЗДАЕМ ВРЕМЕННОЕ СОСТОЯНИЕ С НОВЫМ GRID
        val tempState = state.copy(
            grid = newGrid
        )

        // 5. ВЫЧИСЛЯЕМ ПРОГРЕСС
        val newProgress = calculateProgress(tempState)

        // 6. СОЗДАЕМ ФИНАЛЬНОЕ СОСТОЯНИЕ
        val newState = tempState.copy(
            progress = newProgress
        )

        // 6. ПРОВЕРЯЕМ УСЛОВИЕ ПОБЕДЫ (после удаления оно точно false)
        // Но проверяем для полноты
        val isWin = checkWinCondition(newState)

        // 7. ВОЗВРАЩАЕМ РЕЗУЛЬТАТ
        return SuccessGameResult(
            newState = newState.copy(isCompleted = isWin),
            isWin = isWin
        )
    }

    override fun checkWinCondition(state: FlowFreeState): Boolean {

        // 1. БЫСТРАЯ ПРОВЕРКА: ВСЕ ЯЧЕЙКИ ЗАПОЛНЕНЫ?
        val allCellsFilled = state.grid.all { row ->
            row.all { cell ->
                cell !is FlowFreeCell.Empty
            }
        }

        if (!allCellsFilled) return false

        // 2. ПОЛУЧАЕМ ВСЕ ENDPOINT С ГРУППИРОВКОЙ ПО ЦВЕТУ
        val endpointsByColor = getEndpointsByColor(state)

        // 3. ПРОВЕРЯЕМ ЧТО У КАЖДОГО ЦВЕТА РОВНО 2 ENDPOINT
        val hasValidEndpoints = endpointsByColor.all { (_, endpoints) ->
            val isValid = endpoints.size == 2
            isValid
        }

        if (!hasValidEndpoints) return false

        // 4. ДЛЯ КАЖДОГО ЦВЕТА ПРОВЕРЯЕМ СОЕДИНЕНИЕ ENDPOINT
        val allEndpointsConnected = endpointsByColor.all { (color, endpoints) ->
            val point1 = endpoints[0]
            val point2 = endpoints[1]
            val areConnected = areEndpointsConnected(state, point1, point2, color)
            areConnected
        }

        if (!allEndpointsConnected) return false

        // 5. ПРОВЕРКА НА ПЕРЕСЕЧЕНИЕ ЛИНИЙ (опционально, но важно)
        val hasLineIntersections = checkForLineIntersections(state)
        // 6. ВСЕ ПРОВЕРКИ ПРОЙДЕНЫ - ПОБЕДА!
        return !hasLineIntersections
    }

    override fun isValidPosition(
        state: FlowFreeState,
        position: FlowFreePosition
    ): Boolean {
        return validateBasicPosition(state, position) == null
    }

    override fun arePositionsAdjacent(
        pos1: FlowFreePosition,
        pos2: FlowFreePosition
    ): Boolean {
        val rowDiff = abs(pos1.row - pos2.row)
        val colDiff = abs(pos1.col - pos2.col)
        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1)
    }

    override fun getCellColor(
        state: FlowFreeState,
        position: FlowFreePosition
    ): Int? {
        return getCell(state, position)?.color
    }

    override fun applyPathToGrid(
        grid: List<List<FlowFreeCell>>,
        path: List<FlowFreePosition>,
        color: Int
    ): List<List<FlowFreeCell>> {
        // Создаем mutable копию для преобразования
        val newGrid = grid.map { it.toMutableList() }

        // Применяем путь к grid
        for (position in path) {
            val currentCell = grid[position.row][position.col]
            // Если это уже Endpoint - оставляем как Endpoint
            // Иначе создаем Path
            newGrid[position.row][position.col] = when (currentCell) {
                is FlowFreeCell.Endpoint -> currentCell
                else -> FlowFreeCell.Path(color)
            }
        }

        return newGrid.map { it.toList() }
    }

    /**
     * ПРОВЕРИТЬ БАЗОВУЮ ВАЛИДНОСТЬ ПОЗИЦИИ
     *
     * @param state Текущее состояние
     * @param position Позиция для проверки
     * @return null если позиция валидна, иначе ошибка
     */
    private fun validateBasicPosition(
        state: FlowFreeState,
        position: FlowFreePosition
    ): FlowFreeError? {
        return when {
            // Позиция вне сетки
            position.row !in 0 until state.grid.size -> FlowFreeError.INVALID_POSITION
            position.col !in 0 until state.grid[0].size -> FlowFreeError.INVALID_POSITION
            else -> null
        }
    }

    /**
     * ПРОВЕРИТЬ ЯВЛЯЕТСЯ ЛИ ЯЧЕЙКА PATH
     */
    private fun isPath(
        state: FlowFreeState,
        position: FlowFreePosition
    ): Boolean {
        val cell = state.grid.getOrNull(position.row)
            ?.getOrNull(position.col)
        return cell is FlowFreeCell.Path
    }

    /**
     * ПОЛУЧИТЬ ЯЧЕЙКУ ПО ПОЗИЦИИ
     */
    private fun getCell(
        state: FlowFreeState,
        position: FlowFreePosition
    ): FlowFreeCell? {
        return state.grid.getOrNull(position.row)
            ?.getOrNull(position.col)
    }

    /**
     * ПРОВЕРИТЬ ЕСТЬ ЛИ УЖЕ ЛИНИЯ ДЛЯ ЦВЕТА
     *
     * Линия считается существующей если есть хотя бы одна Path ячейка этого цвета
     */
    override fun hasLineForColor(
        state: FlowFreeState,
        color: Int
    ): Boolean {
        return state.grid.any { row ->
            row.any { cell ->
                cell is FlowFreeCell.Path && cell.color == color
            }
        }
    }

    /**
     * ПОЛУЧИТЬ ЦВЕТ ПУТИ ИЗ ПЕРВОЙ ЯЧЕЙКИ
     */
    private fun getPathColor(
        state: FlowFreeState,
        path: List<FlowFreePosition>
    ): Int? {
        if (path.isEmpty()) return null
        val firstCell = getCell(state, path.first())
        return firstCell?.color
    }

    /**
     * ОБРАБОТКА BACKTRACK (возврат по линии)
     */
    private fun handleBacktrack(
        currentPath: List<FlowFreePosition>,
        backtrackPosition: FlowFreePosition,
    ): LineContinueResult {
        val index = currentPath.indexOf(backtrackPosition)
        if (index < 0) {
            // Не должно случиться, но для безопасности
            return LineContinueResult.Error(
                error = FlowFreeError.INVALID_POSITION,
                position = backtrackPosition
            )
        }

        // Удаляем все позиции после backtrackPosition
        val newPath = currentPath.take(index + 1)
        return LineContinueResult.Success(newPath = newPath)
    }

    /**
     * ПРОВЕРКА ЯЧЕЙКИ ДЛЯ ДОБАВЛЕНИЯ В ЛИНИЮ
     */
    private fun validateCellForLine(
        state: FlowFreeState,
        position: FlowFreePosition,
        lineColor: Int,
    ): FlowFreeError? {
        val cell = getCell(state, position) ?: return FlowFreeError.INVALID_POSITION

        return when (cell) {
            // Пустая ячейка - можно
            is FlowFreeCell.Empty -> null

            // Endpoint того же цвета - можно (завершение линии)
            is FlowFreeCell.Endpoint -> {
                if (cell.color == lineColor) null
                else FlowFreeError.WRONG_COLOR_CONNECTION
            }

            // Path того же цвета - можно (продолжение существующей линии)
            is FlowFreeCell.Path -> {
                if (cell.color == lineColor) null
                else FlowFreeError.CELL_OCCUPIED
            }
        }
    }

    /**
     * ПРОВЕРКА ПРАВИЛ ЛИНИЙ (не пересечение, не разрыв)
     */
    private fun validateLineRules(
        state: FlowFreeState,
        currentPath: List<FlowFreePosition>,
        newPosition: FlowFreePosition,
        lineColor: Int
    ): FlowFreeError? {
        // Правило 1: Нельзя проходить через другие Endpoint
        val cell = getCell(state, newPosition)
        if (cell is FlowFreeCell.Endpoint && cell.color != lineColor) {
            return FlowFreeError.CROSSING_ENDPOINT
        }

        // Правило 2: Проверка на пересечение линий
        // (проверяем соседей новой позиции кроме последней в пути)
        val lastPosition = currentPath.last()
        val neighbors = getAdjacentPositions(newPosition)

        for (neighbor in neighbors) {
            if (neighbor == lastPosition) continue // это откуда пришли

            val neighborCell = getCell(state, neighbor)
            if (neighborCell is FlowFreeCell.Path && neighborCell.color != lineColor) {
                // Есть соседняя ячейка другого цвета - возможное пересечение
                if (isPath(state, newPosition) || isPath(state, lastPosition)) {
                    return FlowFreeError.CROSSING_LINES
                }
            }
        }

        // Правило 3: Если текущая ячейка уже Path, проверяем что не разрываем линию
        if (isPath(state, newPosition)) {
            return validateNotBreakingLine(state, newPosition, lineColor)
        }

        return null
    }

    /**
     * ПРОВЕРКА ЧТО НЕ РАЗРЫВАЕМ СУЩЕСТВУЮЩУЮ ЛИНИЮ
     */
    private fun validateNotBreakingLine(
        state: FlowFreeState,
        position: FlowFreePosition,
        lineColor: Int
    ): FlowFreeError? {
        // Получаем всех соседей этого цвета
        val sameColorNeighbors = getAdjacentPositions(position)
            .filter { neighbor ->
                val cell = getCell(state, neighbor)
                cell?.color == lineColor
            }

        // Если у Path ячейки больше 2 соседей того же цвета - это пересечение
        if (sameColorNeighbors.size > 2) {
            return FlowFreeError.CROSSING_LINES
        }

        return null
    }

    /**
     * ПОЛУЧЕНИЕ СОСЕДНИХ ПОЗИЦИЙ
     */
    private fun getAdjacentPositions(position: FlowFreePosition): List<FlowFreePosition> {
        return listOf(
            FlowFreePosition(position.row - 1, position.col), // вверх
            FlowFreePosition(position.row + 1, position.col), // вниз
            FlowFreePosition(position.row, position.col - 1), // влево
            FlowFreePosition(position.row, position.col + 1)  // вправо
        )
    }

    /**
     * ПРОВЕРКА ДОСТИГЛИ ЛИ ВТОРОЙ ТОЧКИ
     */
    private fun checkIfReachedEndpoint(
        state: FlowFreeState,
        position: FlowFreePosition,
        lineColor: Int
    ): Boolean {
        val cell = getCell(state, position)
        return cell is FlowFreeCell.Endpoint && cell.color == lineColor
    }

    /**
     * ПОШАГОВАЯ ПРОВЕРКА ВСЕГО ПУТИ
     */
    private fun validateCompletePath(
        state: FlowFreeState,
        path: List<FlowFreePosition>,
        lineColor: Int
    ): FlowFreeError? {
        // Временная копия grid для пошаговой проверки
        var tempGrid = state.grid

        for (i in 0 until path.size - 1) {
            val from = path[i]
            val to = path[i + 1]

            // Проверка соседства
            if (!arePositionsAdjacent(from, to)) {
                return FlowFreeError.NOT_ADJACENT_CELL
            }

            // Проверка ячейки 'to' в исходном состоянии
            val validationError = when (val originalCell = getCell(state, to)) {
                // Пустая ячейка - можно
                is FlowFreeCell.Empty -> null

                // Endpoint того же цвета - можно (но только на последнем шаге)
                is FlowFreeCell.Endpoint -> {
                    if (i == path.size - 2 && originalCell.color == lineColor) {
                        null // Это последний шаг к конечной точке
                    } else {
                        FlowFreeError.CROSSING_ENDPOINT
                    }
                }

                // Path или Endpoint другого цвета - нельзя
                else -> FlowFreeError.CELL_OCCUPIED
            }

            if (validationError != null) return validationError

            // Обновляем tempGrid (имитируем применение шага)
            tempGrid = applyStepToGrid(tempGrid, to, lineColor)
        }

        // Дополнительная проверка: путь не должен пересекать сам себя
        if (hasSelfIntersection(path)) return FlowFreeError.CROSSING_LINES

        return null
    }

    /**
     * ПРИМЕНЕНИЕ ОДНОГО ШАГА К GRID
     */
    private fun applyStepToGrid(
        grid: List<List<FlowFreeCell>>,
        to: FlowFreePosition,
        color: Int
    ): List<List<FlowFreeCell>> {
        val newGrid = grid.map { it.toMutableList() }

        // Ячейка 'from' уже должна быть обработана на предыдущем шаге
        // Обрабатываем ячейку 'to'
        val toCell = grid[to.row][to.col]
        newGrid[to.row][to.col] = when (toCell) {
            is FlowFreeCell.Endpoint -> toCell // Оставляем как Endpoint
            else -> FlowFreeCell.Path(color)   // Делаем Path
        }

        return newGrid.map { it.toList() }
    }

    /**
     * ПРОВЕРКА НА САМОПЕРЕСЕЧЕНИЕ ПУТИ
     */
    private fun hasSelfIntersection(path: List<FlowFreePosition>): Boolean {
        // Используем Set для обнаружения дубликатов
        val visited = mutableSetOf<FlowFreePosition>()

        for (position in path) {
            // Пропускаем первую и последнюю точки (они могут быть одинаковыми
            // только если это одна и та же Endpoint, что невозможно)
            if (position == path.first() || position == path.last()) {
                continue
            }

            if (position in visited) return true // Нашли пересечение
            visited.add(position)
        }

        return false
    }

    /**
     * ПОЛУЧИТЬ ПАРЫ ENDPOINT ДЛЯ КАЖДОГО ЦВЕТА
     */
    private fun getEndpointsByColor(state: FlowFreeState): Map<Int, List<FlowFreePosition>> {
        val endpoints = mutableMapOf<Int, MutableList<FlowFreePosition>>()

        for (row in state.grid.indices) {
            for (col in state.grid[row].indices) {
                val cell = state.grid[row][col]
                if (cell is FlowFreeCell.Endpoint) {
                    val color = cell.color
                    val list = endpoints.getOrPut(color) { mutableListOf() }
                    list.add(FlowFreePosition(row, col))
                }
            }
        }

        return endpoints
    }

    /**
     * УДАЛИТЬ ЦВЕТ ИЗ GRID
     *
     * @param grid Исходный grid
     * @param color Цвет для удаления
     * @param endpointsToKeep Endpoint этого цвета (не удалять)
     * @return Новый grid без Path указанного цвета
     */
    private fun removeColorFromGrid(
        grid: List<List<FlowFreeCell>>,
        color: Int,
        endpointsToKeep: List<FlowFreePosition>
    ): List<List<FlowFreeCell>> {
        return grid.mapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, cell ->
                val position = FlowFreePosition(rowIndex, colIndex)
                when {
                    // Endpoint этого цвета - оставляем
                    position in endpointsToKeep -> cell

                    // Path этого цвета - заменяем на Empty
                    cell is FlowFreeCell.Path && cell.color == color -> {
                        FlowFreeCell.Empty
                    }

                    // Все остальное - оставляем как есть
                    else -> cell
                }
            }
        }
    }

    /**
     * ПРОВЕРИТЬ СОЕДИНЕНЫ ЛИ ДВЕ ТОЧКИ
     * Использует BFS для поиска пути между точками
     */
    private fun areEndpointsConnected(
        state: FlowFreeState,
        point1: FlowFreePosition,
        point2: FlowFreePosition,
        color: Int
    ): Boolean {
        // Если это одна и та же точка (не должно быть)
        if (point1 == point2) return false

        val visited = mutableSetOf<FlowFreePosition>()
        val queue = ArrayDeque<FlowFreePosition>()

        // Начинаем с первой точки
        queue.add(point1)
        visited.add(point1)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            // Если дошли до второй точки - соединены!
            if (current == point2) return true

            // Ищем соседей того же цвета
            val neighbors = getAdjacentPositions(current)
                .filter { neighbor ->
                    // Позиция в пределах сетки
                    val row = neighbor.row
                    val col = neighbor.col
                    row in 0 until state.grid.size &&
                            col in 0 until state.grid[0].size
                }
                .filter { neighbor ->
                    // Ячейка того же цвета (Path или Endpoint)
                    val cell = state.grid[neighbor.row][neighbor.col]
                    cell.color == color
                }
                .filter { neighbor ->
                    // Еще не посещали
                    neighbor !in visited
                }

            for (neighbor in neighbors) {
                visited.add(neighbor)
                queue.add(neighbor)
            }
        }
        // Не нашли путь до второй точки
        return false
    }

    /**
     * ПРОВЕРИТЬ НА ПЕРЕСЕЧЕНИЕ ЛИНИЙ
     * Пересечение = Path ячейка имеет больше 2 соседей того же цвета
     */
    private fun checkForLineIntersections(state: FlowFreeState): Boolean {
        for (row in state.grid.indices) {
            for (col in state.grid[row].indices) {
                val cell = state.grid[row][col]

                if (cell is FlowFreeCell.Path) {
                    val position = FlowFreePosition(row, col)
                    val sameColorNeighbors = getAdjacentPositions(position)
                        .filter { neighbor ->
                            val (nRow, nCol) = neighbor
                            nRow in 0 until state.grid.size &&
                                    nCol in 0 until state.grid[0].size
                        }
                        .count { neighbor ->
                            val neighborCell = state.grid[neighbor.row][neighbor.col]
                            neighborCell.color == cell.color
                        }

                    // Path ячейка может иметь максимум 2 соседа того же цвета
                    // (вход и выход линии). Если больше - это пересечение.
                    if (sameColorNeighbors > 2) return true
                }
            }
        }
        return false
    }

    /**
     * ВЫЧИСЛИТЬ ОБЩИЙ ПРОГРЕСС ИГРЫ
     *
     * 💡 ФОРМУЛА:
     * Прогресс = Σ(длина_завершенного_пути_цвета) / общее_количество_ячеек × 100%
     *
     * Где длина пути включает:
     * - 2 Endpoint точки (всегда)
     * - Все Path ячейки этого цвета
     *
     * @param state Текущее состояние игры
     * @return Прогресс от 0 до 100
     */
    fun calculateProgress(state: FlowFreeState): Int {
        val totalCells = state.grid.size * state.grid[0].size

        // Получаем все завершенные пути с их длинами
        val completedPathLengths = getCompletedPathLengths(state)
        val totalCompletedLength = completedPathLengths.values.sum()

        return if (totalCells > 0) {
            (totalCompletedLength * 100 / totalCells).coerceIn(0..100)
        } else {
            0
        }
    }

    /**
     * ПОЛУЧИТЬ ДЛИНЫ ВСЕХ ЗАВЕРШЕННЫХ ПУТЕЙ
     *
     * @param state Текущее состояние
     * @return Map<цвет, длина_пути> для всех завершенных цветов
     */
    private fun getCompletedPathLengths(state: FlowFreeState): Map<Int, Int> {
        val result = mutableMapOf<Int, Int>()

        // Группируем Endpoint по цветам
        val endpointsByColor = getEndpointsByColor(state)

        // Для каждого цвета проверяем завершен ли путь
        for ((color, endpoints) in endpointsByColor) {
            if (endpoints.size == 2 && areEndpointsConnected(
                    state,
                    endpoints[0],
                    endpoints[1],
                    color
                )
            ) {
                // Вычисляем длину пути (включая оба Endpoint)
                val pathLength = calculatePathLengthForColor(state, color)
                result[color] = pathLength
            }
        }

        return result
    }

    /**
     * ВЫЧИСЛИТЬ ДЛИНУ ПУТИ ДЛЯ ЦВЕТА
     *
     * 💡 СЧИТАЕТ:
     * - 2 Endpoint точки (всегда для завершенного пути)
     * - Все Path ячейки этого цвета
     *
     * @param state Текущее состояние
     * @param color Цвет для расчета
     * @return Общее количество ячеек этого цвета в grid
     */
    private fun calculatePathLengthForColor(state: FlowFreeState, color: Int): Int {
        var count = 0

        for (row in state.grid) {
            for (cell in row) {
                when (cell) {
                    is FlowFreeCell.Endpoint -> if (cell.color == color) count++
                    is FlowFreeCell.Path -> if (cell.color == color) count++
                    else -> {}
                }
            }
        }

        return count
    }
}