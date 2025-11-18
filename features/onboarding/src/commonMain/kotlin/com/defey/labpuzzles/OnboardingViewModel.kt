package com.defey.labpuzzles

import com.defey.labpuzzles.base.NavigationManager
import com.defey.labpuzzles.base_viewModel.BaseViewModel

class OnboardingViewModel(
    private val navigationManager: NavigationManager,
) : BaseViewModel<
        OnboardingContract.OnboardingEvent,
        OnboardingContract.OnboardingState,
        OnboardingContract.OnboardingAction>(
    initialState = OnboardingContract.OnboardingState()
) {

    override suspend fun handleEvent(event: OnboardingContract.OnboardingEvent) {
        TODO("Not yet implemented")
    }
}