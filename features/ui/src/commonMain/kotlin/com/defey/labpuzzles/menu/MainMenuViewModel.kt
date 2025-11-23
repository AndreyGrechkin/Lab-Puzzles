package com.defey.labpuzzles.menu

import com.defey.labpuzzles.base.NavigationManager
import com.defey.labpuzzles.base_viewModel.BaseViewModel

class MainMenuViewModel(
    private val navigationManager: NavigationManager,
) : BaseViewModel<
        MainMenuUiContract.Event,
        MainMenuUiContract.State,
        MainMenuUiContract.Action>(
    initialState = MainMenuUiContract.State()
) {

    override suspend fun handleEvent(event: MainMenuUiContract.Event) {
        TODO("Not yet implemented")
    }
}