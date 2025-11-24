package com.defey.labpuzzles.models

sealed class Screen(val route: String) {
    data object SplashScreen : Screen("splash")
    data object MainMenuScreen : Screen("mainMenu")
    data object AchievementsScreen : Screen("achievements")
    data object SettingsScreen : Screen("settings")
    data object GameHubScreen : Screen("gameHub")
    data class Settings(val data: UserData) : Screen("settings/{userName}?score={score}") {
        companion object {
            const val ROUTE = "settings/{userName}?score={score}"
            fun createRoute(userName: String, score: Int = 0) = "settings/$userName?score=$score"
        }
    }
}