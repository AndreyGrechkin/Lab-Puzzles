package com.defey.labpuzzles.water_sort

import com.defey.labpuzzles.models.ErrorGameResult
import com.defey.labpuzzles.models.SuccessGameResult
import com.defey.labpuzzles.models.Vial
import com.defey.labpuzzles.models.WaterSortError
import com.defey.labpuzzles.models.WaterSortState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
}