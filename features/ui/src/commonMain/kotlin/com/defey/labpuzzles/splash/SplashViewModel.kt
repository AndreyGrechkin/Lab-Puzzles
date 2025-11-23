package com.defey.labpuzzles.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defey.labpuzzles.base.NavigationManager
import com.defey.labpuzzles.models.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val navigationManager: NavigationManager,
) : ViewModel() {

    fun navigateToMainMenu() {
        viewModelScope.launch {
            delay(2000)
            navigationManager.clearBackStack(Screen.MainMenuScreen)
        }
    }
}