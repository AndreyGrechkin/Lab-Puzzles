package com.defey.labpuzzles.settings

import com.defey.labpuzzles.base.NavigationManager
import com.defey.labpuzzles.base_viewModel.BaseViewModel

class SettingsViewModel(
    private val navigationManager: NavigationManager,
) : BaseViewModel<SettingsUiContract.Event,
        SettingsUiContract.State,
        SettingsUiContract.Action>(initialState = SettingsUiContract.State()) {

    override suspend fun handleEvent(event: SettingsUiContract.Event) {

    }
}