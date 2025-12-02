package com.defey.labpuzzles.di

import com.defey.labpuzzles.flow_free.FlowFreeEngineImpl
import com.defey.labpuzzles.repository.FlowFreeEngine
import org.koin.dsl.module

val flowFreeModule
    get() = module {
        factory<FlowFreeEngine> { FlowFreeEngineImpl() }

    }