package com.defey.labpuzzles.models

object WaterSortColors {
    const val RED = 0xFFFF3B30.toInt()        // Ярко-красный
    const val BLUE = 0xFF007AFF.toInt()       // Синий (iOS blue)
    const val GREEN = 0xFF34C759.toInt()      // Зеленый (iOS green)
    const val YELLOW = 0xFFFFCC00.toInt()     // Желтый
    const val PINK = 0xFFDC67F8.toInt()       // Лаймовый
    const val BROWN = 0xFFA2845E.toInt()      // Коричневый
    const val DEEP_PURPLE = 0xFF7B1FA2.toInt() // Темно-фиолетовый
    const val CYAN = 0xFF00BCD4.toInt()       // Циан

    val fullColorPalette = listOf(
        RED,
        BLUE,
        GREEN,
        YELLOW,
        PINK,
        BROWN,
        DEEP_PURPLE,
        CYAN
    )
}