package com.defey.labpuzzles.models

sealed class Screen(val route: String) {
    data object SplashScreen : Screen("splash")
    data object MainMenuScreen : Screen("mainMenu")
    data object AchievementsScreen : Screen("achievements")
    data object SettingsScreen : Screen("settings")
    data object ChaptersScreen : Screen("chapters")
    data class GameHubScreen(val chapter: Chapter) : Screen("gameHub/{$ARGS_CHAPTER}") {
        companion object {
            const val ROUTE = "gameHub/{$ARGS_CHAPTER}"
            fun createRoute(chapter: Chapter) = "gameHub/${chapter.name}"
        }
    }

    data class WaterSortScreen(val levelId: String) : Screen("waterSort/{$ARGS_LEVEL_ID}") {
        companion object {
            const val ROUTE = "waterSort/{$ARGS_LEVEL_ID}"
            fun createRoute(levelId: String) = "waterSort/${levelId}"
        }
    }

    data class FlowFreeScreen(val levelId: String) : Screen("flowFree/{$ARGS_LEVEL_ID}") {
        companion object {
            const val ROUTE = "flowFree/{$ARGS_LEVEL_ID}"
            fun createRoute(levelId: String) = "flowFree/${levelId}"
        }
    }

    data class Settings(val data: UserData) : Screen("settings/{userName}?score={score}") {
        companion object {
            const val ROUTE = "settings/{userName}?score={score}"
            fun createRoute(userName: String, score: Int = 0) = "settings/$userName?score=$score"
        }
    }

    companion object {
        const val ARGS_LEVEL_ID = "levelId"
        const val ARGS_CHAPTER = "chapter"
    }
}