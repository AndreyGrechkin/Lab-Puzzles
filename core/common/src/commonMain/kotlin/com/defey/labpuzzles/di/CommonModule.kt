package com.defey.labpuzzles.di

import com.defey.labpuzzles.managers.localization.LanguageManager
import com.defey.labpuzzles.managers.localization.LanguageManagerImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val commonModule
    get() = module {
//        factory<CommonTimer> { TimerFactory.create() }
        single<LanguageManager> { LanguageManagerImpl(get(), get()) }
    }

val languageModule
    get() = module {
        includes(platformLanguageModule())
    }

internal expect fun platformLanguageModule(): Module