package com.defey.labpuzzles.models

enum class FlowFreeError : GameError {
    // Базовые ошибки
    INVALID_POSITION,           // Позиция вне сетки
    NOT_AN_ENDPOINT,            // Попытка начать не с Endpoint
    NOT_ADJACENT_CELL,          // Ячейки не соседние
    CELL_OCCUPIED,              // Ячейка уже занята

    // Ошибки линий
    WRONG_COLOR_CONNECTION,     // Попытка соединить точки разных цветов
    CROSSING_LINES,             // Линии пересекаются
    CROSSING_ENDPOINT,          // Линия проходит через чужую точку
}