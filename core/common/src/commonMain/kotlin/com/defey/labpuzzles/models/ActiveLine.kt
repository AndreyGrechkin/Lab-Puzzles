package com.defey.labpuzzles.models

data class ActiveLine(
    val path: List<FlowFreePosition>,  // Последовательность ячеек
    val color: Int                     // Цвет линии
) {
    /**
     * Минимальная проверка: линия имеет хотя бы 2 точки
     * Реальная валидация - в движке
     */
    val canBeCompleted: Boolean get() = path.size >= 2
}
