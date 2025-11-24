package com.defey.labpuzzles.achievements

import com.defey.labpuzzles.base.NavigationManager
import com.defey.labpuzzles.base_viewModel.BaseViewModel

class AchievementsViewModel(
    private val navigationManager: NavigationManager,
) : BaseViewModel<AchievementsUiContract.Event,
        AchievementsUiContract.State,
        AchievementsUiContract.Action>
    (initialState = AchievementsUiContract.State()) {


    override suspend fun handleEvent(event: AchievementsUiContract.Event) {

    }
}