package com.defey.labpuzzles.di

import com.defey.labpuzzles.repository.WaterSortEngine
import com.defey.labpuzzles.water_sort.WaterSortEngineImpl
import org.koin.dsl.module

val waterSortEngineModule
    get() = module {
        factory<WaterSortEngine> { WaterSortEngineImpl() }
    }