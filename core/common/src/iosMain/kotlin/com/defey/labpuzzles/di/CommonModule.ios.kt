package com.defey.labpuzzles.di

import com.defey.labpuzzles.managers.localization.IosLocalization
import com.defey.labpuzzles.managers.localization.Localization
import com.defey.labpuzzles.managers.timer.CommonTimer
import com.defey.labpuzzles.managers.timer.CommonTimerImpl
import com.defey.labpuzzles.managers.timer.ForwardTimer
import com.defey.labpuzzles.managers.timer.ForwardTimerImpl
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun platformLanguageModule(): Module = module {
    single<Localization> { IosLocalization() }
}

actual val timerModule: Module
    get() = module {
        factory<CommonTimer> { CommonTimerImpl() }
        factory<ForwardTimer> { ForwardTimerImpl() }
    }