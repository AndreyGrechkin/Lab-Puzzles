package com.defey.labpuzzles.achievements

import com.defey.labpuzzles.base_viewModel.UiAction
import com.defey.labpuzzles.base_viewModel.UiEvent
import com.defey.labpuzzles.base_viewModel.UiState

class AchievementsUiContract {

    data class State(
        val name: String = "Achievements Screen"
    ) : UiState

    sealed interface Event : UiEvent {

    }

    sealed interface Action : UiAction {

    }
}