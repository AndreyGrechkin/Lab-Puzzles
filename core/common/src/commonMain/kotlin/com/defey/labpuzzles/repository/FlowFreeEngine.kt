package com.defey.labpuzzles.repository

import com.defey.labpuzzles.models.FlowFreePosition
import com.defey.labpuzzles.models.FlowFreeState
import com.defey.labpuzzles.models.GameResult

interface FlowFreeEngine {

    /**
     * ВЫПОЛНЕНИЕ ХОДА - проведение линии
     *
     * Игрок нажимает на точку и тянет линию до следующей ячейки.
     * Линия проводится пошагово между соседними ячейками.
     *
     * @param state Текущее состояние игры
     * @param from Начальная позиция (должна быть точкой)
     * @param to Целевая позиция (должна быть соседней по вертикали/горизонтали)
     * @return Результат выполнения хода
     */
    fun makeMove(
        state: FlowFreeState,
        from: FlowFreePosition,
        to: FlowFreePosition
    ): GameResult<FlowFreeState>

    /**
     * УДАЛЕНИЕ ВСЕЙ ЛИНИИ ЦВЕТА
     *
     * Двойной тап на точку удаляет всю линию этого цвета.
     * Точки остаются на месте, удаляется только путь между ними.
     *
     * @param state Текущее состояние
     * @param color Цвет линии для удаления
     * @return Новое состояние после удаления
     */
    fun clearLine(
        state: FlowFreeState,
        color: Int
    ): GameResult<FlowFreeState>

    /**
     * ПРОВЕРКА УСЛОВИЯ ПОБЕДЫ
     *
     * Условия победы:
     * 1. Все точки соединены линиями своего цвета
     * 2. Все ячейки поля заполнены (путь или точки)
     * 3. Линии не пересекаются
     * 4. Каждая пара точек соединена ровно одной линией
     *
     * @param state Состояние для проверки
     * @return true если игра завершена
     */
    fun checkWinCondition(state: FlowFreeState): Boolean

    /**
     * ПРОВЕРКА ВАЛИДНОСТИ ХОДА
     *
     * Вспомогательный метод для UI.
     * Проверяет можно ли выполнить ход без его фактического выполнения.
     *
     * @param state Текущее состояние
     * @param from Начальная позиция
     * @param to Целевая позиция
     * @return true если ход допустим
     */
    fun isValidMove(
        state: FlowFreeState,
        from: FlowFreePosition,
        to: FlowFreePosition
    ): Boolean
}