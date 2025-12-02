package com.defey.labpuzzles.models

data class FlowFreeState(
    val grid: List<List<FlowFreeCell>>,
    override val movesCount: Int = 0,
    override val isCompleted: Boolean = false
) : GameState() {

    /**
     * Количество строк в поле
     */
    val rows: Int get() = grid.size

    /**
     * Количество колонок в поле (берем первую строку как эталон)
     */
    val cols: Int get() = if (grid.isNotEmpty()) grid[0].size else 0

    /**
     * Проверяет валидность координат
     */
    fun isValidPosition(position: FlowFreePosition): Boolean {
        return position.row in 0 until rows &&
                position.col in 0 until cols
    }

    /**
     * Получает ячейку по позиции
     */
    fun getCell(position: FlowFreePosition): FlowFreeCell? {
        return if (isValidPosition(position)) {
            grid[position.row][position.col]
        } else {
            null
        }
    }

    /**
     * Является ли ячейка точкой (Endpoint)
     */
    fun isEndpoint(position: FlowFreePosition): Boolean {
        return getCell(position) is FlowFreeCell.Endpoint
    }

    /**
     * Является ли ячейка частью пути (Path)
     */
    fun isPath(position: FlowFreePosition): Boolean {
        return getCell(position) is FlowFreeCell.Path
    }

    /**
     * Является ли ячейка пустой
     */
    fun isEmpty(position: FlowFreePosition): Boolean {
        return getCell(position) is FlowFreeCell.Empty
    }

    /**
     * Получает цвет ячейки
     */
    fun getCellColor(position: FlowFreePosition): Int? {
        return getCell(position)?.color
    }
}
