package com.defey.labpuzzles.watersort

import androidx.lifecycle.SavedStateHandle
import com.defey.labpuzzles.base.NavigationManager
import com.defey.labpuzzles.base_viewModel.BaseViewModel
import com.defey.labpuzzles.managers.timer.ForwardTimer
import com.defey.labpuzzles.models.Difficulty
import com.defey.labpuzzles.models.ErrorGameResult
import com.defey.labpuzzles.models.SuccessGameResult
import com.defey.labpuzzles.models.Vial
import com.defey.labpuzzles.models.WaterSortState
import com.defey.labpuzzles.repository.WaterSortLevelGenerator
import com.defey.labpuzzles.repository.WaterSortEngine
import com.defey.labpuzzles.water_sort.PourAnimationState
import com.defey.labpuzzles.water_sort.PourDirection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WaterSortViewModel(
    savedStateHandle: SavedStateHandle,
    private val navigationManager: NavigationManager,
    private val waterSortEngine: WaterSortEngine,
    private val forwardTimer: ForwardTimer,
    private val levelGenerator: WaterSortLevelGenerator
) : BaseViewModel<WaterSortUiContract.Event,
        WaterSortUiContract.State,
        WaterSortUiContract.Action
        >(initialState = WaterSortUiContract.State()) {

    private val _animationState = MutableStateFlow<PourAnimationState>(PourAnimationState.Idle)
    val animationState: StateFlow<PourAnimationState> = _animationState.asStateFlow()
    private var currentLevel: List<Vial> = emptyList()

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
            WaterSortUiContract.Event.OnRetry -> {
                forwardTimer.stop()
                updateState {
                   copy(vials = emptyList())
                }
                updateState {
                    copy(
                        vials = currentLevel,
                        movesCount = 0,
                        selectedVialIndex = null,
                        isLevelCompleted = false,
                        elapsedTime = 0L
                    )
                }
                startTimer()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        forwardTimer.dispose()
    }

    private fun startNewGame() {
        viewModelScope.launch(Dispatchers.Default) {
                val resultLevel = levelGenerator.generateLevel(state.value.levelId, Difficulty.EXPERT)
                withContext(Dispatchers.Main) {
                    forwardTimer.stop()
                    _animationState.value = PourAnimationState.Idle
                    currentLevel = resultLevel
                    updateState {
                        copy(
                            vials = resultLevel,
                            movesCount = 0,
                            selectedVialIndex = null,
                            isLevelCompleted = false,
                            elapsedTime = 0L
                        )
                    }
                    startTimer()
                }
            }
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
            animateProgress(300L) { progress ->
                _animationState.value = PourAnimationState.SelectingVial(selectedIndex, progress)
            }
            _animationState.value = PourAnimationState.VialLifted(selectedIndex)
        }
    }

    private fun startDeselectAnimation(selectedIndex: Int) {
        viewModelScope.launch {
            _animationState.value = PourAnimationState.DeselectingVial(selectedIndex, 0f)
            animateProgress(300L) { progress ->
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
            animateProgress(400L) { progress ->
                _animationState.value =
                    PourAnimationState.MovingToTarget(fromIndex, toIndex, direction, progress)
            }

            _animationState.value =
                PourAnimationState.PouringStream(fromIndex, toIndex, direction, color, 0f)
            animateProgress(400L) { progress ->
                _animationState.value =
                    PourAnimationState.PouringStream(fromIndex, toIndex, direction, color, progress)
            }

            _animationState.value =
                PourAnimationState.ReturningBack(fromIndex, toIndex, direction, 0f)
            animateProgress(400L) { progress ->
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