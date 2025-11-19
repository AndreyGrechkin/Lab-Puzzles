package com.defey.labpuzzles.models

enum class WaterSortError: GameError {
    VALID,                    // ✅ Ход правильный (нужен для удобства)
    INVALID_SOURCE,           // Попытка перелить из пустой колбы
    INVALID_TARGET,           // Попытка перелить в полную колбу
    SAME_COLOR_REQUIRED,      // Цвета не совпадают
    SAME_VIAL                 // Попытка перелить в ту же колбу
}