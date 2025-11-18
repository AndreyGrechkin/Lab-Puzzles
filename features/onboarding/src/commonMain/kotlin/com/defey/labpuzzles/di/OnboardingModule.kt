package com.defey.labpuzzles.di

import com.defey.labpuzzles.OnboardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val onboardingModule = module {
    viewModel { OnboardingViewModel(get()) }
}