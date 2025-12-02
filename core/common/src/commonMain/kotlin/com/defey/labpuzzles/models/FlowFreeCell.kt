package com.defey.labpuzzles.models

sealed class FlowFreeCell {
    abstract val color: Int?

    // Пустая ячейка
    object Empty : FlowFreeCell() {
        override val color: Int? = null
    }

    // Точка (начальная или конечная)
    data class Endpoint(override val color: Int) : FlowFreeCell()

    // Путь (линия между точками)
    data class Path(override val color: Int) : FlowFreeCell()
}