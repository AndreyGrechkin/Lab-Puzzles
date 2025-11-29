package com.defey.labpuzzles.watersort

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import com.defey.labpuzzles.base.NavigationManager
import com.defey.labpuzzles.base_viewModel.BaseViewModel
import com.defey.labpuzzles.managers.timer.ForwardTimer
import com.defey.labpuzzles.models.ErrorGameResult
import com.defey.labpuzzles.models.SuccessGameResult
import com.defey.labpuzzles.models.Vial
import com.defey.labpuzzles.models.WaterSortState
import com.defey.labpuzzles.repository.WaterSortEngine
import com.defey.labpuzzles.water_sort.PourAnimationState
import com.defey.labpuzzles.water_sort.PourDirection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class WaterSortViewModel(
    savedStateHandle: SavedStateHandle,
    private val navigationManager: NavigationManager,
    private val waterSortEngine: WaterSortEngine,
    private val forwardTimer: ForwardTimer,
) : BaseViewModel<WaterSortUiContract.Event,
        WaterSortUiContract.State,
        WaterSortUiContract.Action
        >(initialState = WaterSortUiContract.State()) {

    private val _animationState = MutableStateFlow<PourAnimationState>(PourAnimationState.Idle)
    val animationState: StateFlow<PourAnimationState> = _animationState.asStateFlow()

    init {

        val levelId = savedStateHandle.get<String>("levelId") ?: ""
        updateState {
            copy(levelId = levelId)
        }
        startNewGame()
    }

    override suspend fun handleEvent(event: WaterSortUiContract.Event) {
        when (event) {
            is WaterSortUiContract.Event.OnAddCurrencyClick -> {}
            WaterSortUiContract.Event.OnBackClick -> {
                navigationManager.popBackStack()
            }

            is WaterSortUiContract.Event.OnVialClick -> onVialSelected(event.index, event.direction)
        }
    }

    override fun onCleared() {
        super.onCleared()
        forwardTimer.dispose()
    }

    private fun startNewGame() {
        // Моковые данные для тестирования
        forwardTimer.stop()
        _animationState.value = PourAnimationState.Idle
        val initialVials = createTestLevel()

        updateState {
            copy(
                vials = initialVials,
                movesCount = 0,
                selectedVialIndex = null,
                isLevelCompleted = false,
                elapsedTime = 0L
            )
        }

        startTimer()
    }

    private fun createTestLevel(): List<Vial> {
        return listOf(
            // 7 разных цветов + 1 пустая пробирка
            Vial(colors = listOf(Color.Red, Color.Red, Color.Blue, Color.Blue)),
            Vial(colors = listOf(Color.Blue, Color.Green, Color.Green)),
            Vial(colors = listOf(Color.Yellow, Color.Yellow, Color.Green)),
            Vial(colors = listOf(Color.Yellow, Color.Yellow, Color.Blue)),
            Vial(colors = listOf(Color.Green)),
            Vial(colors = listOf(Color.Red, Color.Red)),

            )
    }

    private fun startTimer() {
        forwardTimer.start(
            intervalMillis = 1000,
            onTick = { elapsedMillis ->
                if (!state.value.isLevelCompleted) {
                    val elapsedSeconds = elapsedMillis / 1000
                    updateState { copy(elapsedTime = elapsedSeconds) }
                } else {
                    forwardTimer.stop()
                }
            }
        )
    }

    private fun onVialSelected(selectedIndex: Int, direction: PourDirection?) {
        if (_animationState.value !is PourAnimationState.Idle &&
            _animationState.value !is PourAnimationState.VialLifted
        ) {
            return
        }

        val currentState = state.value
        when (currentState.selectedVialIndex) {
            null -> {
                if (!currentState.vials[selectedIndex].isEmpty) {
                    updateState { copy(selectedVialIndex = selectedIndex) }
                    startSelectAnimation(selectedIndex)
                }
            }

            selectedIndex -> {
                updateState { copy(selectedVialIndex = null) }
                startDeselectAnimation(selectedIndex)
            }

            else -> {
                val fromIndex = currentState.selectedVialIndex
                val result = waterSortEngine.makeMove(
                    state = WaterSortState(currentState.vials, currentState.movesCount),
                    fromIndex = fromIndex,
                    toIndex = selectedIndex
                )

                when (result) {
                    is SuccessGameResult<*> -> {
                        if (result.isWin) forwardTimer.stop()
                        direction?.let {
                            startPourAnimation(fromIndex, selectedIndex, result.newState, it)
                        }
                    }

                    is ErrorGameResult<*, *> -> startInvalidMoveAnimation(fromIndex)
                }
            }
        }
    }

    private fun startSelectAnimation(selectedIndex: Int) {
        viewModelScope.launch {
            _animationState.value = PourAnimationState.SelectingVial(selectedIndex, 0f)
            animateProgress(500L) { progress ->
                _animationState.value = PourAnimationState.SelectingVial(selectedIndex, progress)
            }
            _animationState.value = PourAnimationState.VialLifted(selectedIndex)
        }
    }

    private fun startDeselectAnimation(selectedIndex: Int) {
        viewModelScope.launch {
            _animationState.value = PourAnimationState.DeselectingVial(selectedIndex, 0f)
            animateProgress(500L) { progress ->
                _animationState.value = PourAnimationState.DeselectingVial(selectedIndex, progress)
            }
            _animationState.value = PourAnimationState.Idle
        }
    }

    private fun startPourAnimation(
        fromIndex: Int,
        toIndex: Int,
        newState: WaterSortState,
        direction: PourDirection
    ) {
        viewModelScope.launch {
            val sourceVial = state.value.vials[fromIndex]
            val color = sourceVial.topColor ?: return@launch

            _animationState.value =
                PourAnimationState.MovingToTarget(fromIndex, toIndex, direction, 0f)
            animateProgress(1000L) { progress ->
                _animationState.value =
                    PourAnimationState.MovingToTarget(fromIndex, toIndex, direction, progress)
            }

            _animationState.value =
                PourAnimationState.PouringStream(fromIndex, toIndex, direction, color, 0f)
            animateProgress(1000L) { progress ->
                _animationState.value =
                    PourAnimationState.PouringStream(fromIndex, toIndex, direction, color, progress)
            }

            _animationState.value =
                PourAnimationState.ReturningBack(fromIndex, toIndex, direction, 0f)
            animateProgress(1000L) { progress ->
                _animationState.value =
                    PourAnimationState.ReturningBack(fromIndex, toIndex, direction, progress)
            }

            updateState {
                copy(
                    selectedVialIndex = null,
                    vials = newState.vials,
                    movesCount = newState.movesCount,
                    isLevelCompleted = newState.isCompleted
                )
            }
            if (newState.isCompleted) onLevelComplete()
            _animationState.value = PourAnimationState.Idle
        }
    }

    private fun startInvalidMoveAnimation(selectedIndex: Int) {
        viewModelScope.launch {
            _animationState.value = PourAnimationState.InvalidMoveShake(selectedIndex, 0f)
            animateProgress(300L) { progress ->
                _animationState.value = PourAnimationState.InvalidMoveShake(selectedIndex, progress)
            }
            _animationState.value = PourAnimationState.VialLifted(selectedIndex)
        }
    }

    private suspend fun animateProgress(duration: Long, onProgress: (Float) -> Unit) {
        val startTime = forwardTimer.getCurrentTime()
        while (true) {
            val elapsed = forwardTimer.getCurrentTime() - startTime
            val progress = (elapsed.toFloat() / duration).coerceIn(0f, 1f)
            onProgress(progress)

            if (progress >= 1f) break
            delay(16)
        }
    }


    private fun onLevelComplete() {
        viewModelScope.launch {
            // TODO: Сохранение прогресса, награды
            println("Уровень завершен! Ходов: ${state.value.movesCount}, Время: ${state.value.elapsedTime}с")
        }
    }

    fun onUndo() {
        // TODO: Реализация отмены хода
        println("Отмена хода")
    }

    fun onHint() {
        // TODO: Реализация подсказки
        println("Показать подсказку")
    }

    fun onRestart() {
        startNewGame()
    }

    fun onSkip() {
        // TODO: Реализация пропуска уровня
        println("Пропуск уровня")
    }
}