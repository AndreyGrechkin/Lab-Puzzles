package com.defey.labpuzzles.di

import com.defey.labpuzzles.achievements.AchievementsViewModel
import com.defey.labpuzzles.chapters.ChaptersViewModel
import com.defey.labpuzzles.gameHub.GameHubViewModel
import com.defey.labpuzzles.menu.MainMenuViewModel
import com.defey.labpuzzles.settings.SettingsViewModel
import com.defey.labpuzzles.splash.SplashViewModel
import com.defey.labpuzzles.watersort.WaterSortViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featuresUiModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { MainMenuViewModel(get(), get()) }
    viewModel { AchievementsViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { GameHubViewModel(get(), get()) }
    viewModel { WaterSortViewModel(get(), get(), get(), get(), get()) }
    viewModel { ChaptersViewModel(get()) }
}