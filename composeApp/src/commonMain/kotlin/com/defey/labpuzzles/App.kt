package com.defey.labpuzzles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.defey.labpuzzles.achievements.screen.AchievementsScreen
import com.defey.labpuzzles.base.NavigationCommand
import com.defey.labpuzzles.base.NavigationManager
import com.defey.labpuzzles.gameHub.screens.GameHubScreen
import com.defey.labpuzzles.managers.localization.LanguageManager
import com.defey.labpuzzles.menu.screens.MainMenuScreen
import com.defey.labpuzzles.models.Screen
import com.defey.labpuzzles.settings.screens.SettingsScreen
import com.defey.labpuzzles.splash.screens.SplashScreen
import com.defey.labpuzzles.theme.AppTheme
import com.defey.labpuzzles.utils.createOrientationController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    AppTheme(darkTheme = false) {
        val navController = rememberNavController()
        val navigationManager: NavigationManager = koinInject<NavigationManager>()
        val languageManager: LanguageManager = koinInject<LanguageManager>()
        val navigationState by navigationManager.navigationState.collectAsState()
        val languageState by languageManager.languageFlow.collectAsState()
        val orientationController = createOrientationController()

        LaunchedEffect(navigationState, languageState) {
            when (val command = navigationState) {
                is NavigationCommand.Navigate -> {
                    navController.navigate(command.route) {
                        command.options.popUpToRoute?.let {
                            popUpTo(0) {
                                inclusive = command.options.inclusive
                                saveState = command.options.saveState
                            }
                        }
                        restoreState = command.options.restoreState
                    }
                    navigationManager.clearNavigation()
                }

                is NavigationCommand.PopBackStack -> {
                    navController.popBackStack()
                    navigationManager.clearNavigation()
                }

                is NavigationCommand.PopBackStackTo -> {
                    navController.popBackStack(command.route, command.inclusive)
                    navigationManager.clearNavigation()
                }

                NavigationCommand.Idle -> {}
            }
        }

        CompositionLocalProvider(
            LocalOrientationController provides orientationController
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.SplashScreen.route
            ) {
                composable(Screen.SplashScreen.route) {
                    SplashScreen()
                }

                composable(Screen.MainMenuScreen.route) {
                    MainMenuScreen()
                }

                composable(Screen.AchievementsScreen.route) {
                    AchievementsScreen()
                }

                composable(Screen.SettingsScreen.route) {
                    SettingsScreen()
                }

                composable(Screen.GameHubScreen.route) {
                    GameHubScreen()
                }

                composable(
                    route = Screen.Settings.ROUTE,
                    arguments = listOf(
                        navArgument("userName") {
                            type = NavType.StringType
                        },
                        navArgument("score") {
                            type = NavType.IntType
                            defaultValue = 0
                        }
                    )
                ) {
                    MainMenuScreen()
                }
            }
        }
    }
}