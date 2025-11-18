package com.defey.labpuzzles.di

import com.defey.labpuzzles.managers.localization.IosLocalization
import com.defey.labpuzzles.managers.localization.Localization
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun platformLanguageModule(): Module = module {
    single<Localization> { IosLocalization() }
}