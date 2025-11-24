package com.defey.labpuzzles.menu

import com.defey.labpuzzles.base_viewModel.UiAction
import com.defey.labpuzzles.base_viewModel.UiEvent
import com.defey.labpuzzles.base_viewModel.UiState

class MainMenuUiContract {

    data class State(
        val version: String,
        val hasSavedGame: Boolean = false,
    ) : UiState

    sealed interface Event : UiEvent {
        data object OnContinueGame : Event
        data object OnNewGameClicked : Event
        data object OnNewGame : Event
        data object OnAchievements : Event
        data object OnSettings : Event
    }

    sealed interface Action : UiAction {
        data object ShowNewGameDialog : Action
    }
}