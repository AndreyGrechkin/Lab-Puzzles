package com.defey.labpuzzles.menu

import com.defey.labpuzzles.base_viewModel.UiAction
import com.defey.labpuzzles.base_viewModel.UiEvent
import com.defey.labpuzzles.base_viewModel.UiState

class MainMenuUiContract {

    data class State(
        val name: String = "Welcome MainMenuScreen"
    ) : UiState

    sealed interface Event : UiEvent {

    }

    sealed interface Action : UiAction {

    }
}