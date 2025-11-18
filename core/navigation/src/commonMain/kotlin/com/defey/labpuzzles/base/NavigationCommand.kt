package com.defey.labpuzzles.base

sealed class NavigationCommand {
    object Idle : NavigationCommand()
    data class Navigate(
        val route: String,
        val options: NavOptions = NavOptions()
    ) : NavigationCommand()
    object PopBackStack : NavigationCommand()
    data class PopBackStackTo(
        val route: String,
        val inclusive: Boolean = false
    ) : NavigationCommand()
}