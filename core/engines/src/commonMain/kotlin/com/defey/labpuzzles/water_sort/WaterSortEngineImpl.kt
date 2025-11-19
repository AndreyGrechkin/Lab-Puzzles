package com.defey.labpuzzles.water_sort

import com.defey.labpuzzles.models.ErrorGameResult
import com.defey.labpuzzles.models.GameResult
import com.defey.labpuzzles.models.WaterSortError
import com.defey.labpuzzles.models.SuccessGameResult
import com.defey.labpuzzles.models.Vial
import com.defey.labpuzzles.models.WaterSortState
import com.defey.labpuzzles.repository.WaterSortEngine

class WaterSortEngineImpl : WaterSortEngine {

    override fun makeMove(
        state: WaterSortState,
        fromIndex: Int,
        toIndex: Int
    ): GameResult<WaterSortState> {
        val validity = checkMoveValidity(state.vials, fromIndex, toIndex)

        if (validity != WaterSortError.VALID) {
            return ErrorGameResult(state, validity)
        }

        val newVials = performMove(state.vials, fromIndex, toIndex)
        val newState = state.copy(
            vials = newVials,
            movesCount = state.movesCount + 1
        )

        val isWin = checkWinCondition(newVials)

        return SuccessGameResult(
            newState = newState.copy(isCompleted = isWin),
            isWin = isWin
        )
    }

    override fun checkWinCondition(vials: List<Vial>): Boolean {
        return vials.all { vial ->
            vial.isEmpty || (vial.colors.distinct().size == 1 && vial.colors.size == vial.capacity)
        }
    }

    private fun checkMoveValidity(vials: List<Vial>, fromIndex: Int, toIndex: Int): WaterSortError {
        if (fromIndex == toIndex) return WaterSortError.SAME_VIAL

        val fromVial = vials[fromIndex]
        val toVial = vials[toIndex]

        // Проверяем что из колбы можно переливать
        if (fromVial.isEmpty || fromVial.topColor == null) {
            return WaterSortError.INVALID_SOURCE
        }

        // Проверяем что в целевую колбу можно переливать
        if (toVial.isFull) {
            return WaterSortError.INVALID_TARGET
        }

        // Проверяем совпадение цвета (если целевая колба не пустая)
        if (!toVial.isEmpty && toVial.topColor != fromVial.topColor) {
            return WaterSortError.SAME_COLOR_REQUIRED
        }

        return WaterSortError.VALID
    }

    private fun performMove(vials: List<Vial>, fromIndex: Int, toIndex: Int): List<Vial> {
        val newVials = vials.toMutableList()
        val fromVial = newVials[fromIndex]
        val toVial = newVials[toIndex]

        // Определяем сколько жидкости можно перелить
        val availableSpace = toVial.capacity - toVial.colors.size
        val movableLiquid = minOf(fromVial.topColorCount, availableSpace)
        val colorToMove = fromVial.topColor!!

        // Создаем новые колбы
        val newFromColors = fromVial.colors.dropLast(movableLiquid)
        val newToColors = toVial.colors + List(movableLiquid) { colorToMove }

        newVials[fromIndex] = fromVial.copy(colors = newFromColors)
        newVials[toIndex] = toVial.copy(colors = newToColors)

        return newVials
    }
}