package com.defey.labpuzzles.di

import com.defey.labpuzzles.water_sort.WaterSortDifficultyConfig
import com.defey.labpuzzles.repository.WaterSortLevelGenerator
import com.defey.labpuzzles.repository.WaterSortEngine
import com.defey.labpuzzles.water_sort.WaterSortDifficultyConfigImpl
import com.defey.labpuzzles.water_sort.WaterSortEngineImpl
import com.defey.labpuzzles.water_sort.WaterSortLevelGeneratorImpl
import org.koin.dsl.module

val waterSortEngineModule
    get() = module {
        factory<WaterSortEngine> { WaterSortEngineImpl() }
        factory<WaterSortLevelGenerator> { WaterSortLevelGeneratorImpl(get()) }
        factory<WaterSortDifficultyConfig> { WaterSortDifficultyConfigImpl() }
    }