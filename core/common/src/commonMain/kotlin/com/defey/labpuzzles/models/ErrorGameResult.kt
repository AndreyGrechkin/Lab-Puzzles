package com.defey.labpuzzles.models

/**
 * SEALED INTERFACE - базовый тип для ошибок любой игры
 *
 * 💡 ЗАЧЕМ НУЖЕН:
 * - Общий интерфейс для ошибок всех игр
 * - Каждая игра реализует свои конкретные ошибки
 */
sealed interface GameError

/**
 * ERROR RESULT с дженериком для типа ошибки
 *
 * 💡 ДЖЕНЕРИК <E : GameError>:
 * - E - конкретный тип ошибок для каждой игры
 * - WaterSort использует WaterSortError
 * - SlidingPuzzle использует SlidingPuzzleError
 */
data class ErrorGameResult<T : GameState, E : GameError>(
    override val newState: T,
    val error: E  // ← Теперь типобезопасные ошибки для каждой игры
) : GameResult<T> {
    override val isValid: Boolean = false
    override val isWin: Boolean = false
}