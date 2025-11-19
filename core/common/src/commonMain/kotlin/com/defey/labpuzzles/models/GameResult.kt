package com.defey.labpuzzles.models

/**
 * SEALED INTERFACE - результат выполнения хода в игре
 *
 * 💡 ЗАЧЕМ НУЖЕН:
 * - Типобезопасная обработка успешных/неуспешных ходов
 * - Pattern matching в UI ("when (result) { is Success -> ... }")
 *
 * 📋 ДЖЕНЕРИК <T : GameState>:
 * - T - конкретный тип состояния (WaterSortState, FlowFreeState и т.д.)
 * - Гарантирует, что результат соответствует типу игры
 */

sealed interface GameResult<out T : GameState> {
    val isValid: Boolean
    val newState: T
    val isWin: Boolean
}