package com.defey.labpuzzles.flow_free

import androidx.lifecycle.SavedStateHandle
import com.defey.labpuzzles.base.NavigationManager
import com.defey.labpuzzles.base_viewModel.BaseViewModel
import com.defey.labpuzzles.models.ActiveLine
import com.defey.labpuzzles.models.ErrorGameResult
import com.defey.labpuzzles.models.FlowFreeCell
import com.defey.labpuzzles.models.FlowFreeError
import com.defey.labpuzzles.models.FlowFreePosition
import com.defey.labpuzzles.models.FlowFreeState
import com.defey.labpuzzles.models.LineContinueResult
import com.defey.labpuzzles.models.LineStartResult
import com.defey.labpuzzles.models.Screen.Companion.ARGS_LEVEL_ID
import com.defey.labpuzzles.models.SuccessGameResult
import com.defey.labpuzzles.models.WaterSortColors
import com.defey.labpuzzles.repository.FlowFreeEngine

class FlowFreeViewModel(
    savedStateHandle: SavedStateHandle,
    private val navigationManager: NavigationManager,
    private val engine: FlowFreeEngine,
) : BaseViewModel<FlowFreeUiContract.Event,
        FlowFreeUiContract.State,
        FlowFreeUiContract.Action>(initialState = FlowFreeUiContract.State()) {

    init {
        val levelId = savedStateHandle.get<String>(ARGS_LEVEL_ID) ?: ""
        updateState {
            copy(levelId = levelId)
        }
        getTestLevel()
    }

    override suspend fun handleEvent(event: FlowFreeUiContract.Event) {
        when (event) {
            FlowFreeUiContract.Event.OnBackClick -> navigationManager.popBackStack()
            is FlowFreeUiContract.Event.OnRetry -> handleRetry()
            is FlowFreeUiContract.Event.OnErrorShown -> handleErrorShown()
            is FlowFreeUiContract.Event.OnDragStart -> handleDragStart(event.position)
            is FlowFreeUiContract.Event.OnDragMove -> handleDragMove(event.position)
            FlowFreeUiContract.Event.OnDragEnd -> handleDragEnd()
            FlowFreeUiContract.Event.OnDragCancel -> handleDragCancel()
            is FlowFreeUiContract.Event.OnLineDelete -> {}
            is FlowFreeUiContract.Event.OnEndpointTap -> handleEndpointTap(event.position)
        }
    }

    private fun getTestLevel() {
        val grid = listOf(
            // Ряд 0: R . G . Y
            listOf(
                FlowFreeCell.Endpoint(WaterSortColors.RED),
                FlowFreeCell.Empty,
                FlowFreeCell.Endpoint(WaterSortColors.GREEN),
                FlowFreeCell.Empty,
                FlowFreeCell.Endpoint(WaterSortColors.YELLOW)
            ),
            // Ряд 1: . . B . P
            listOf(
                FlowFreeCell.Empty,
                FlowFreeCell.Empty,
                FlowFreeCell.Endpoint(WaterSortColors.BLUE),
                FlowFreeCell.Empty,
                FlowFreeCell.Endpoint(WaterSortColors.PINK)
            ),
            // Ряд 2: . . . . .
            listOf(
                FlowFreeCell.Empty,
                FlowFreeCell.Empty,
                FlowFreeCell.Empty,
                FlowFreeCell.Empty,
                FlowFreeCell.Empty
            ),
            // Ряд 3: . G . Y .
            listOf(
                FlowFreeCell.Empty,
                FlowFreeCell.Endpoint(WaterSortColors.GREEN),
                FlowFreeCell.Empty,
                FlowFreeCell.Endpoint(WaterSortColors.YELLOW),
                FlowFreeCell.Empty
            ),
            // Ряд 4: . R B P .
            listOf(
                FlowFreeCell.Empty,
                FlowFreeCell.Endpoint(WaterSortColors.RED),
                FlowFreeCell.Endpoint(WaterSortColors.BLUE),
                FlowFreeCell.Endpoint(WaterSortColors.PINK),
                FlowFreeCell.Empty
            )
        )

        updateState {
            copy(
                movesCount = 0,
                progress = 0,
                isLevelCompleted = false,
                grid = grid,
            )
        }
    }


    /**
     * НАЧАЛО DRAG - палец коснулся экрана
     */
    private fun handleDragStart(position: FlowFreePosition) {
        val currentState = createFlowFreeState()
        val activeLine = state.value.activeLine

        // СЦЕНАРИЙ 1: Есть активная линия
        if (activeLine != null) {
            // Проверяем тап на последней точке активной линии
            if (position == activeLine.path.last()) {
                // Ничего не делаем - линия остается активной
                return
            }
        }

        // СЦЕНАРИЙ 2: Начинаем новую линию через движок
        when (val result = engine.startNewLine(currentState, position)) {
            is LineStartResult.Success -> {
                updateState {
                    copy(
                        activeLine = ActiveLine(
                            path = listOf(position),
                            color = result.lineColor
                        ),
                        showError = false,
                        errorType = null,
                        errorPosition = null
                    )
                }
            }

            is LineStartResult.Error -> {
                showError(result.error, result.position)
            }
        }
    }

    /**
     * DRAG MOVE - палец двигается
     */
    private fun handleDragMove(newPosition: FlowFreePosition) {
        val activeLine = state.value.activeLine ?: run {
            return
        }
        val currentState = createFlowFreeState().copy(activeLine = null)
        val result = engine.continueLine(
            state = currentState,
            currentPath = activeLine.path,
            newPosition = newPosition
        )

        when (result) {
            is LineContinueResult.Success -> {
                updateState {
                    copy(
                        activeLine = activeLine.copy(path = result.newPath),
                        showError = false,
                        errorType = null,
                        errorPosition = null
                    )
                }
                // Если достигли конечной точки - можно сразу завершить
                if (result.reachedEndpoint) {
                    handleDragEnd()
                }
            }

            is LineContinueResult.Error -> {
                showError(result.error, result.position)
            }
        }
    }

    /**
     * DRAG END - палец отпущен
     */
    private fun handleDragEnd() {
        val activeLine = state.value.activeLine ?: run {
            return
        }
        // Проверяем что линия может быть завершена
        if (!activeLine.canBeCompleted) {
            resetActiveLine()
            return
        }

        // Получаем текущее состояние БЕЗ активной линии
        val currentState = createFlowFreeState().copy(activeLine = null)
        when (val result = engine.completeLine(currentState, activeLine.path)) {
            is SuccessGameResult -> {
                // Создаем CompletedPath
                val completedPath = FlowFreeUiContract.CompletedPath(
                    positions = activeLine.path,
                    color = activeLine.color
                )

                // Добавляем к существующим путям
                val currentPaths = state.value.completedPaths
                val newPaths = currentPaths + completedPath
                // Сохраняем изменения из движка + новый путь
                updateState {
                    copy(
                        grid = result.newState.grid,
                        movesCount = result.newState.movesCount,
                        isLevelCompleted = result.newState.isCompleted,
                        progress = result.newState.progress,
                        activeLine = null,
                        completedPaths = newPaths,
                        showError = false,
                        errorType = null,
                        errorPosition = null
                    )
                }
                // Проверяем победу
                if (result.isWin) {
                    handleLevelComplete()
                }
            }

            is ErrorGameResult<*, *> -> {
                val error = result.error as? FlowFreeError
                // Показываем ошибку, но оставляем активную линию для исправления
                if (error != null && activeLine.path.isNotEmpty()) {
                    showError(error, activeLine.path.last())
                }
            }
        }
    }

    /**
     * DRAG CANCEL - жест отменен системой
     */
    private fun handleDragCancel() {
        resetActiveLine()
    }

    /**
     * ОБРАБОТКА ТАПА НА ТОЧКЕ - УДАЛЕНИЕ ЛИНИИ
     */
    private fun handleEndpointTap(position: FlowFreePosition) {
        val currentState = createFlowFreeState()
        // 1. ПРОВЕРКА: ЯЧЕЙКА СУЩЕСТВУЕТ И ЭТО ENDPOINT
        val cell = currentState.grid.getOrNull(position.row)
            ?.getOrNull(position.col)

        if (cell == null) return

        if (cell !is FlowFreeCell.Endpoint) return

        val color = cell.color

        // 2. ПРОВЕРКА: ЕСТЬ ЛИ ЛИНИЯ ЭТОГО ЦВЕТА
        if (!engine.hasLineForColor(currentState, color)) return

        // 3. УДАЛЕНИЕ ЛИНИИ ЧЕРЕЗ ДВИЖОК
        when (val result = engine.removeLine(currentState, color)) {
            is SuccessGameResult -> {
                // 4. ОБНОВЛЕНИЕ СОСТОЯНИЯ
                updateState {
                    copy(
                        // Обновляем grid из движка
                        grid = result.newState.grid,
                        // Удаляем путь этого цвета из completedPaths
                        completedPaths = completedPaths.filterNot { it.color == color },
                        progress = result.newState.progress,
                        // Сбрасываем ошибки если были
                        showError = false,
                        errorType = null,
                        errorPosition = null
                    )
                }
            }

            is ErrorGameResult<*, *> -> {
                val error = result.error as? FlowFreeError
                // Показываем ошибку если что-то пошло не так
                if (error != null) {
                    showError(error, position)
                }
            }
        }
    }


    // ============================================
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // ============================================

    /**
     * СОЗДАТЬ FLOWFREESTATE ИЗ ТЕКУЩЕГО UI STATE
     */
    private fun createFlowFreeState(): FlowFreeState {
        return FlowFreeState(
            grid = state.value.grid,
            movesCount = state.value.movesCount,
            isCompleted = state.value.isLevelCompleted,
            activeLine = state.value.activeLine
        )
    }

    /**
     * ПОКАЗАТЬ ОШИБКУ
     */
    private fun showError(error: FlowFreeError, position: FlowFreePosition) {
        updateState {
            copy(
                showError = true,
                errorType = error,
                errorPosition = position
            )
        }
    }

    /**
     * СБРОСИТЬ АКТИВНУЮ ЛИНИЮ
     */
    private fun resetActiveLine() {
        updateState {
            copy(
                activeLine = null,
                showError = false,
                errorType = null,
                errorPosition = null
            )
        }
    }

    /**
     * ЗАВЕРШЕНИЕ УРОВНЯ
     */
    private fun handleLevelComplete() {
        // TODO: Сохранение прогресса, награды, переход к следующему уровню
        // Например:
        // saveProgress()
        // showVictoryScreen()
        // unlockNextLevel()
    }

    /**
     * ПЕРЕЗАПУСК УРОВНЯ
     */
    private fun handleRetry() {
        //TODO: Retry Level
        resetActiveLine()
        // Очищаем completed paths
        updateState {
            copy(completedPaths = emptyList())
        }
    }

    /**
     * ОШИБКА ПОКАЗАНА (сброс)
     */
    private fun handleErrorShown() {
        updateState {
            copy(
                showError = false,
                errorType = null,
                errorPosition = null
            )
        }
    }

//    1. Отмена незавершенной линии (ViewModel):
//    fun cancelActiveLine() {
//        val activeLine = state.activeLine ?: return
//        val result = engine.removeLine(state, activeLine.color)
//        // Обработка результата
//    }

//    2. Кнопка "Undo" (отмена последней линии):
//    fun undoLastLine() {
//        // Нужно хранить историю или знать какую линию удалить
//        val lastLineColor = getLastLineColor()
//        val result = engine.removeLine(state, lastLineColor)
//    }

//    3. Очистка уровня (рестарт):
//    fun restartLevel() {
//        // Удаляем все линии
//        val colors = getAllLineColors()
//        var currentState = state
//        for (color in colors) {
//            val result = engine.removeLine(currentState, color)
//            currentState = result.newState
//        }
//    }
}