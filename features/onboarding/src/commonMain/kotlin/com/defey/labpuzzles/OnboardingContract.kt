package com.defey.labpuzzles

import com.defey.labpuzzles.base_viewModel.UiAction
import com.defey.labpuzzles.base_viewModel.UiEvent
import com.defey.labpuzzles.base_viewModel.UiState

object OnboardingContract {

    data class OnboardingState(
        val name: String = "Welcome OnboardingScreen"
    ) : UiState

    sealed interface OnboardingEvent : UiEvent {

    }

    sealed interface OnboardingAction : UiAction {

    }
}