package com.defey.labpuzzles.gameHub

import com.defey.labpuzzles.base_viewModel.UiAction
import com.defey.labpuzzles.base_viewModel.UiEvent
import com.defey.labpuzzles.base_viewModel.UiState

class GameHubUiContract {

    data class State(
        val name: String = "GameHubScreen"
    ) : UiState

    sealed interface Event : UiEvent {

    }

    sealed interface Action : UiAction {

    }
}