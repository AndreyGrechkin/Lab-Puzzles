package com.defey.labpuzzles.models

/**
 * УСПЕШНЫЙ результат хода
 *
 * 💡 ИСПОЛЬЗУЕТСЯ КОГДА:
 * - Игрок сделал правильный ход
 * - Игра продолжается или завершена победой
 */
data class SuccessGameResult<T : GameState>(
    override val newState: T,
    override val isWin: Boolean = false
) : GameResult<T> {
    override val isValid: Boolean = true
}