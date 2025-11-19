package com.defey.labpuzzles.di

import com.defey.labpuzzles.OnboardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featuresUiModule = module {
    viewModel { OnboardingViewModel(get()) }
}