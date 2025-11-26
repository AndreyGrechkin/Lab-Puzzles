package com.defey.labpuzzles.models

import androidx.compose.ui.graphics.Color

enum class GameType(val color: Color) {
    WATER_SORT(Color(0xFF2196F3)),    // Синий
    FLOW_FREE(Color(0xFF9C27B0)),     // Фиолетовый
    SUDOKU(Color(0xFFFF9800)),        // Оранжевый
    SLIDING_PUZZLE(Color(0xFF4CAF50)) // Зеленый
}