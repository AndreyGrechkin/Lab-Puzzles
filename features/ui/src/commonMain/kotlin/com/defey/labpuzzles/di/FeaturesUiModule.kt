package com.defey.labpuzzles.di

import com.defey.labpuzzles.menu.MainMenuViewModel
import com.defey.labpuzzles.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featuresUiModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { MainMenuViewModel(get()) }
}