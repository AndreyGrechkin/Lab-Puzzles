package com.defey.labpuzzles.models

enum class FlowFreeError : GameError {
    // Попытка хода в несуществующую ячейку
    INVALID_POSITION,

    // Попытка начать линию не с точки
    NOT_AN_ENDPOINT,

    // Ячейка уже занята другим цветом
    CELL_OCCUPIED,

    // Линии пересекаются
    CROSSING_LINES,

    // Линия не соединяет точки того же цвета
    WRONG_COLOR_CONNECTION,

    // Попытка удалить точку
    CANNOT_REMOVE_ENDPOINT,

    // Ход вне допустимого направления (не по вертикали/горизонтали)
    INVALID_DIRECTION,

    // Разрыв существующей линии
    BREAKING_LINE,

    // Попытка провести линию через другую точку
    CROSSING_ENDPOINT,

    // Линия не смежная (ход не в соседнюю ячейку)
    NOT_ADJACENT_CELL
}