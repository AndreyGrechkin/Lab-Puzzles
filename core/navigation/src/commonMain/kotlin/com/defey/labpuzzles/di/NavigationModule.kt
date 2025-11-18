package com.defey.labpuzzles.di

import com.defey.labpuzzles.base.NavigationManager
import org.koin.dsl.module

val navigationModule = module {
    single<NavigationManager> { NavigationManager() }
}