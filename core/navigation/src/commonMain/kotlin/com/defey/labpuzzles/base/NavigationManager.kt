package com.defey.labpuzzles.base

import com.defey.labpuzzles.models.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationManager {
    private val _navigationState = MutableStateFlow<NavigationCommand>(NavigationCommand.Idle)
    val navigationState = _navigationState.asStateFlow()

    fun navigate(route: String) {
        _navigationState.value = NavigationCommand.Navigate(route)
    }

    fun navigate(route: String, navOptions: NavOptions) {
        _navigationState.value = NavigationCommand.Navigate(route, navOptions)
    }

    fun popBackStack() {
        _navigationState.value = NavigationCommand.PopBackStack
    }

    fun popBackStack(route: String, inclusive: Boolean = false) {
        _navigationState.value = NavigationCommand.PopBackStackTo(route, inclusive)
    }

    fun navigate(screen: Screen) {
        _navigationState.value = NavigationCommand.Navigate(screen.route)
    }

    fun navigate(screen: Screen, navOptions: NavOptions) {
        _navigationState.value = NavigationCommand.Navigate(screen.route, navOptions)
    }

    fun popBackStack(screen: Screen, inclusive: Boolean = false) {
        _navigationState.value = NavigationCommand.PopBackStackTo(screen.route, inclusive)
    }

    fun clearBackStack(newDestination: Screen) {
        _navigationState.value = NavigationCommand.Navigate(
            newDestination.route,
            NavOptions(popUpToRoute = null, inclusive = true)
        )
    }

    fun clearNavigation() {
        _navigationState.value = NavigationCommand.Idle
    }
}