package com.defey.labpuzzles.flow_free

import com.defey.labpuzzles.models.ErrorGameResult
import com.defey.labpuzzles.models.FlowFreeCell
import com.defey.labpuzzles.models.FlowFreeError
import com.defey.labpuzzles.models.FlowFreePosition
import com.defey.labpuzzles.models.FlowFreeState
import com.defey.labpuzzles.models.SuccessGameResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FlowFreeEngineTest {

    private val engine = FlowFreeEngineImpl()

    // Тестовые цвета
    private val RED = 0xFFFF0000.toInt()
    private val BLUE = 0xFF0000FF.toInt()
    private val GREEN = 0xFF00FF00.toInt()

    // ============================================
    // ТЕСТОВЫЕ УТИЛИТЫ
    // ============================================

    /**
     * СОЗДАНИЕ ПРОСТОГО ТЕСТОВОГО ПОЛЯ 3x3
     */
    private fun createTestGrid3x3(): List<List<FlowFreeCell>> {
        return listOf(
            // row 0
            listOf(
                FlowFreeCell.Endpoint(RED),    // (0,0)
                FlowFreeCell.Empty,            // (0,1)
                FlowFreeCell.Endpoint(BLUE)    // (0,2)
            ),
            // row 1
            listOf(
                FlowFreeCell.Empty,            // (1,0)
                FlowFreeCell.Empty,            // (1,1)
                FlowFreeCell.Empty            // (1,2)
            ),
            // row 2
            listOf(
                FlowFreeCell.Endpoint(RED),    // (2,0)
                FlowFreeCell.Empty,            // (2,1)
                FlowFreeCell.Endpoint(BLUE)    // (2,2)
            )
        )
    }

    /**
     * СОЗДАНИЕ ПРОСТОГО ТЕСТОВОГО ПОЛЯ 4x4
     */
    // Тестовая сетка:
    // R . .
    // . . .
    // . . B
    val grid = listOf(
        listOf(FlowFreeCell.Endpoint(RED), FlowFreeCell.Empty, FlowFreeCell.Empty),
        listOf(FlowFreeCell.Empty, FlowFreeCell.Empty, FlowFreeCell.Empty),
        listOf(FlowFreeCell.Empty, FlowFreeCell.Empty, FlowFreeCell.Endpoint(BLUE))
    )

    val state = FlowFreeState(grid = grid)

    // СЦЕНАРИЙ 1: Успешное начало на красной точке
    val result1 = engine.startNewLine(state, FlowFreePosition(0, 0))
    // Ожидается: LineStartResult.Success(color=RED, position=(0,0))

    // СЦЕНАРИЙ 2: Неудача - не Endpoint
    val result2 = engine.startNewLine(state, FlowFreePosition(0, 1))
    // Ожидается: LineStartResult.Error(NOT_AN_ENDPOINT, (0,1))

    // СЦЕНАРИЙ 3: Неудача - позиция вне сетки
    val result3 = engine.startNewLine(state, FlowFreePosition(5, 5))
    // Ожидается: LineStartResult.Error(INVALID_POSITION, (5,5))

    // СЦЕНАРИЙ 4: Успешное начало на синей точке
    val result4 = engine.startNewLine(state, FlowFreePosition(2, 2))
    // Ожидается: LineStartResult.Success(color=BLUE, position=(2,2))


}