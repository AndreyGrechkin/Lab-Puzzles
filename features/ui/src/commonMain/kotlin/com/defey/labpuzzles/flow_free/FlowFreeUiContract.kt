package com.defey.labpuzzles.flow_free

import com.defey.labpuzzles.base_viewModel.UiAction
import com.defey.labpuzzles.base_viewModel.UiEvent
import com.defey.labpuzzles.base_viewModel.UiState
import com.defey.labpuzzles.models.ActiveLine
import com.defey.labpuzzles.models.FlowFreeCell
import com.defey.labpuzzles.models.FlowFreeError
import com.defey.labpuzzles.models.FlowFreePosition
import com.defey.labpuzzles.models.PlayerCurrencies

class FlowFreeUiContract {

    data class State(
        // Игровые данные
        val grid: List<List<FlowFreeCell>> = emptyList(),
        val movesCount: Int = 0,
        val isLevelCompleted: Boolean = false,
        val activeLine: ActiveLine? = null,
        val levelId: String = "",
        val progress: Int = 0,
        val currencies: PlayerCurrencies = PlayerCurrencies(),
        val completedPaths: List<CompletedPath> = emptyList(),
        val showError: Boolean = false,
        val errorType: FlowFreeError? = null,
        val errorPosition: FlowFreePosition? = null
    ) : UiState

    sealed interface Event : UiEvent {
        data object OnBackClick : Event
        data object OnRetry : Event
        data object OnErrorShown : Event

        // ЖЕСТЫ (сырые данные от UI)
        data class OnDragStart(val position: FlowFreePosition) : Event
        data class OnDragMove(val position: FlowFreePosition) : Event
        data object OnDragEnd : Event
        data object OnDragCancel : Event
        data class OnLineDelete(val color: Int) : Event
        data class OnEndpointTap(val position: FlowFreePosition) : Event
    }

    sealed interface Action : UiAction {
        // Действия для UI
        data class ShowError(val error: FlowFreeError) : Action
        data object ClearError : Action
        data class UpdateTempLine(val line: List<FlowFreePosition>) : Action
        data object ClearTempLine : Action
    }

    data class CompletedPath(
        val positions: List<FlowFreePosition>,
        val color: Int
    ) {
        val isNotEmpty: Boolean get() = positions.isNotEmpty()
        val size: Int get() = positions.size
    }
}