package com.defey.labpuzzles.menu

import com.defey.labpuzzles.base.NavigationManager
import com.defey.labpuzzles.base_viewModel.BaseViewModel
import com.defey.labpuzzles.models.Screen
import com.defey.labpuzzles.repository.AppInfoProvider
import kotlinx.coroutines.launch

class MainMenuViewModel(
    private val navigationManager: NavigationManager,
    appInfoProvider: AppInfoProvider,
) : BaseViewModel<
        MainMenuUiContract.Event,
        MainMenuUiContract.State,
        MainMenuUiContract.Action>(
    initialState = MainMenuUiContract.State(version = appInfoProvider.appInfo.versionName)
) {


    override suspend fun handleEvent(event: MainMenuUiContract.Event) {
        when (event) {
            MainMenuUiContract.Event.OnAchievements ->
                navigationManager.navigate(Screen.AchievementsScreen)

            MainMenuUiContract.Event.OnContinueGame -> {}
            MainMenuUiContract.Event.OnNewGame ->
                navigationManager.navigate(Screen.GameHubScreen)
            MainMenuUiContract.Event.OnNewGameClicked -> showNewGameDialog()
            MainMenuUiContract.Event.OnSettings ->
                navigationManager.navigate(Screen.SettingsScreen)
        }
    }

    private fun showNewGameDialog() {
        viewModelScope.launch {
            sendAction(MainMenuUiContract.Action.ShowNewGameDialog)
        }
    }
}