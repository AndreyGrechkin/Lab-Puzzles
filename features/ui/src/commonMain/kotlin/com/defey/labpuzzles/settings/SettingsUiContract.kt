package com.defey.labpuzzles.settings

import com.defey.labpuzzles.base_viewModel.UiAction
import com.defey.labpuzzles.base_viewModel.UiEvent
import com.defey.labpuzzles.base_viewModel.UiState

class SettingsUiContract {

    data class State(
        val name: String = "Settings Screen"
    ) : UiState

    sealed interface Event : UiEvent {

    }

    sealed interface Action : UiAction {

    }
}