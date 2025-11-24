package com.defey.labpuzzles.gameHub

import com.defey.labpuzzles.base.NavigationManager
import com.defey.labpuzzles.base_viewModel.BaseViewModel

class GameHubViewModel(
    private val navigationManager: NavigationManager,
) : BaseViewModel<GameHubUiContract.Event,
        GameHubUiContract.State,
        GameHubUiContract.Action>(initialState = GameHubUiContract.State()) {

    override suspend fun handleEvent(event: GameHubUiContract.Event) {

    }
}