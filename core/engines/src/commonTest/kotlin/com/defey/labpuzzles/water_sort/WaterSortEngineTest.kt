package com.defey.labpuzzles.water_sort

import com.defey.labpuzzles.models.ErrorGameResult
import com.defey.labpuzzles.models.SuccessGameResult
import com.defey.labpuzzles.models.Vial
import com.defey.labpuzzles.models.WaterSortError
import com.defey.labpuzzles.models.WaterSortState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class WaterSortEngineTest {
    // СОЗДАЕМ экземпляр движка для всех тестов
    private val engine = WaterSortEngineImpl()

    /**
     * ТЕСТ: Проверяем правильный ход между колбами
     *
     * 💡 ЧТО ПРОВЕРЯЕМ:
     * - Ход должен быть валидным
     * - Количество ходов должно увеличиться
     * - Жидкость должна перейти из одной колбы в другую
     */
    @Test
    fun `test valid move between vials`() {
        // ARRANGE: Подготовка данных
        val initialState = WaterSortState(
            vials = listOf(
                Vial(colors = listOf(1, 1)), // Две красные жидкости
                Vial(colors = listOf(2)),     // Одна синяя
                Vial(colors = listOf())       // Пустая колба
            )
        )

        // ACT: Выполнение действия
        val result = engine.makeMove(initialState, fromIndex = 0, toIndex = 2)

        // ASSERT: Проверка результата
        assertTrue(result.isValid, "Ход должен быть валидным")

        // Приводим тип для доступа к специфичным свойствам
        val successResult = result as SuccessGameResult<WaterSortState>

        assertEquals(1, successResult.newState.movesCount, "Счетчик ходов должен увеличиться")
        assertEquals(
            listOf(),
            successResult.newState.vials[0].colors,
            "В исходной колбе не должна остаться жидкость"
        )
        assertEquals(
            listOf(1, 1),
            successResult.newState.vials[2].colors,
            "В целевой колбе должна появиться жидкость"
        )
    }

    /**
     * ТЕСТ: Попытка перелить в полную колбу
     *
     * 💡 ЧТО ПРОВЕРЯЕМ:
     * - Ход должен быть невалидным
     * - Должна вернуться правильная ошибка
     * - Состояние игры не должно измениться
     */
    @Test
    fun `test invalid move to full vial`() {
        // ARRANGE
        val initialState = WaterSortState(
            vials = listOf(
                Vial(colors = listOf(1)),                    // Одна красная
                Vial(colors = listOf(2, 2, 2, 2))    // Полная колба (4 синих)
            )
        )

        // ACT
        val result = engine.makeMove(initialState, fromIndex = 0, toIndex = 1)

        // ASSERT
        assertFalse(result.isValid, "Ход должен быть невалидным")

        val errorResult = result as ErrorGameResult<*, *>
        assertEquals(
            WaterSortError.INVALID_TARGET,
            errorResult.error,
            "Должна быть ошибка 'неверная цель'"
        )
        assertEquals(
            0,
            errorResult.newState.movesCount,
            "Счетчик ходов не должен измениться при ошибке"
        )
    }
    /**
     * ТЕСТ: Проверка условия победы
     *
     * 💡 ЧТО ПРОВЕРЯЕМ:
     * - Когда все колбы заполнены одним цветом или пустые - игра выиграна
     */
    @Test
    fun `test win condition`() {
        // ARRANGE - выигрышное состояние
        val winState = WaterSortState(
            vials = listOf(
                Vial(colors = listOf(1, 1, 1, 1)), // 4 красных
                Vial(colors = listOf(2, 2, 2, 2)), // 4 синих
                Vial(colors = listOf())             // Пустая
            )
        )

        // ACT - проверяем условие победы напрямую
        val isWin = engine.checkWinCondition(winState.vials)

        // ASSERT
        assertTrue(isWin, "Игра должна определяться как выигранная")
    }

    // Вспомогательные методы для создания Vial
    private fun createVial(colors: List<Int>, capacity: Int = 4): Vial {
        return Vial(colors = colors, capacity = capacity)
    }

    private fun createEmptyVial(capacity: Int = 4): Vial {
        return Vial(colors = emptyList(), capacity = capacity)
    }

    @Test
    fun `valid move should succeed and update state`() {
        // Arrange
        val initialState = WaterSortState(
            vials = listOf(
                createVial(listOf(1, 1)), // [RED, RED]
                createEmptyVial()         // []
            ),
            movesCount = 0
        )

        // Act
        val result = engine.makeMove(initialState, fromIndex = 0, toIndex = 1)

        // Assert
        assertTrue(result is SuccessGameResult)
        assertEquals(1, result.newState.movesCount)
        assertEquals(listOf(), result.newState.vials[0].colors)
        assertEquals(listOf(1,1), result.newState.vials[1].colors)
    }

    @Test
    fun `move to same vial should return error`() {
        // Arrange
        val initialState = WaterSortState(
            vials = listOf(
                createVial(listOf(1, 1)),
                createEmptyVial()
            )
        )

        // Act
        val result = engine.makeMove(initialState, fromIndex = 0, toIndex = 0)

        // Assert
        assertTrue(result is ErrorGameResult<*, *>)
        assertEquals(WaterSortError.SAME_VIAL, (result as ErrorGameResult<*, *>).error)
    }

    @Test
    fun `move from empty vial should return invalid source error`() {
        // Arrange
        val initialState = WaterSortState(
            vials = listOf(
                createEmptyVial(),
                createVial(listOf(1, 1))
            )
        )

        // Act
        val result = engine.makeMove(initialState, fromIndex = 0, toIndex = 1)

        // Assert
        assertTrue(result is ErrorGameResult<*, *>)
        assertEquals(WaterSortError.INVALID_SOURCE, (result as ErrorGameResult<*, *>).error)
    }

    @Test
    fun `move to full vial should return invalid target error`() {
        // Arrange
        val initialState = WaterSortState(
            vials = listOf(
                createVial(listOf(1, 1, 1, 1)), // полная
                createVial(listOf(2, 2, 2, 2))  // полная
            )
        )

        // Act
        val result = engine.makeMove(initialState, fromIndex = 0, toIndex = 1)

        // Assert
        assertTrue(result is ErrorGameResult<*, *>)
        assertEquals(WaterSortError.INVALID_TARGET, (result as ErrorGameResult<*, *>).error)
    }

    @Test
    fun `move to vial with different top color should return same color required error`() {
        // Arrange
        val initialState = WaterSortState(
            vials = listOf(
                createVial(listOf(1, 1)), // RED
                createVial(listOf(2, 2))  // BLUE
            )
        )

        // Act
        val result = engine.makeMove(initialState, fromIndex = 0, toIndex = 1)

        // Assert
        assertTrue(result is ErrorGameResult<*, *>)
        assertEquals(WaterSortError.SAME_COLOR_REQUIRED, (result as ErrorGameResult<*, *>).error)
    }

    @Test
    fun `partial move should transfer correct amount of liquid`() {
        // Arrange
        val initialState = WaterSortState(
            vials = listOf(
                createVial(listOf(1, 1, 1)), // [RED, RED, RED]
                createVial(listOf(2)),       // [BLUE]
                createEmptyVial() // []
            )
        )

        // Act - переливаем из первой колбы в третью (можно перелить 2 RED)
        val result = engine.makeMove(initialState, fromIndex = 0, toIndex = 2)

        // Assert
        assertTrue(result is SuccessGameResult)
        assertEquals(listOf(), result.newState.vials[0].colors) // Остался один RED
        assertEquals(listOf(2), result.newState.vials[1].colors) // Не изменилась
        assertEquals(listOf(1, 1, 1), result.newState.vials[2].colors) // Получили два RED
    }

    @Test
    fun `checkWinCondition should return true when all vials are completed`() {
        // Arrange
        val completedVials = listOf(
            createVial(listOf(1, 1, 1, 1)), // uniform and full
            createVial(listOf(2, 2, 2, 2)), // uniform and full
            createEmptyVial()               // empty
        )

        // Act
        val result = engine.checkWinCondition(completedVials)

        // Assert
        assertTrue(result)
    }

    @Test
    fun `checkWinCondition should return false when vials are mixed`() {
        // Arrange
        val mixedVials = listOf(
            createVial(listOf(1, 2, 1)), // mixed colors
            createEmptyVial()
        )

        // Act
        val result = engine.checkWinCondition(mixedVials)

        // Assert
        assertFalse(result)
    }

    @Test
    fun `checkWinCondition should return false when vial is uniform but not full`() {
        // Arrange
        val uniformButNotFull = listOf(
            createVial(listOf(1, 1)), // uniform but not full
            createEmptyVial()
        )

        // Act
        val result = engine.checkWinCondition(uniformButNotFull)

        // Assert
        assertFalse(result)
    }

    @Test
    fun `checkWinCondition should return true for empty vials`() {
        // Arrange
        val emptyVials = listOf(
            createEmptyVial(),
            createEmptyVial()
        )

        // Act
        val result = engine.checkWinCondition(emptyVials)

        // Assert
        assertTrue(result)
    }

    @Test
    fun `win condition should be detected after successful move`() {
        // Arrange - почти завершенная игра
        val initialState = WaterSortState(
            vials = listOf(
                createVial(listOf(1), capacity = 2),    // [RED]
                createVial(listOf(1), capacity = 2),    // [RED]
                createEmptyVial(capacity = 2)
            )
        )

        // Act - объединяем два RED
        val result = engine.makeMove(initialState, fromIndex = 0, toIndex = 1)

        // Assert
        assertTrue(result is SuccessGameResult)
        assertTrue(result.isWin)
        assertTrue(result.newState.isCompleted)
        assertEquals(listOf(1, 1), result.newState.vials[1].colors) // теперь полная uniform колба
    }

    @Test
    fun `move with multiple same colors should transfer maximum possible`() {
        // Arrange
        val initialState = WaterSortState(
            vials = listOf(
                createVial(listOf(1, 1, 1)), // [RED, RED, RED]
                createVial(listOf(1)),       // [RED]
                createEmptyVial(capacity = 3) // []
            )
        )

        // Act - переливаем из первой колбы в третью
        val result = engine.makeMove(initialState, fromIndex = 0, toIndex = 2)

        // Assert
        assertTrue(result is SuccessGameResult)
        assertEquals(listOf(), result.newState.vials[0].colors) // Остался один RED
        assertEquals(listOf(1, 1, 1), result.newState.vials[2].colors) // Получили два RED
    }

    @Test
    fun `complex win scenario should work correctly`() {
        // Arrange - сложный сценарий с несколькими цветами
        val initialState = WaterSortState(
            vials = listOf(
                createVial(listOf(1, 2, 1, 2)), // mixed
                createVial(listOf(1, 2, 1, 2)), // mixed
                createEmptyVial(),
                createEmptyVial()
            )
        )

        // Act & Assert - симулируем несколько ходов
        var state = initialState

        // Первый ход: переливаем RED в пустую колбу
        var result = engine.makeMove(state, fromIndex = 0, toIndex = 2)
        assertTrue(result is SuccessGameResult)
        state = result.newState

        // Второй ход: переливаем BLUE в другую пустую колбу
        result = engine.makeMove(state, fromIndex = 0, toIndex = 3)
        assertTrue(result is SuccessGameResult)
        state = result.newState

        // Проверяем что игра еще не завершена
        assertFalse(state.isCompleted)
    }

    @Test
    fun `vial properties should work correctly`() {
        // Test Vial properties
        val emptyVial = createEmptyVial()
        val fullVial = createVial(listOf(1, 1, 1, 1))
        val mixedVial = createVial(listOf(1, 2, 1))
        val uniformVial = createVial(listOf(2, 2, 2))

        assertTrue(emptyVial.isEmpty)
        assertFalse(emptyVial.isFull)
        assertNull(emptyVial.topColor)
        assertEquals(0, emptyVial.topColorCount)

        assertTrue(fullVial.isFull)
        assertFalse(fullVial.isEmpty)
        assertEquals(1, fullVial.topColor)
        assertEquals(4, fullVial.topColorCount)

        assertEquals(1, mixedVial.topColor)
        assertEquals(1, mixedVial.topColorCount) // только последний элемент

        assertEquals(2, uniformVial.topColor)
        assertEquals(3, uniformVial.topColorCount) // все три элемента
    }
}