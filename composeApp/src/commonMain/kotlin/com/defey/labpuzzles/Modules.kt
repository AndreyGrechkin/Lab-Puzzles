package com.defey.labpuzzles

import com.defey.labpuzzles.di.commonModule
import com.defey.labpuzzles.di.languageModule
import com.defey.labpuzzles.di.navigationModule
import com.defey.labpuzzles.di.featuresUiModule
import com.defey.labpuzzles.di.storageModule
import org.koin.core.module.Module

expect val versionModule: Module
private val coreModules
    get() = listOf(
        commonModule,
        navigationModule,
//        uiKitModule,
        languageModule,
//        commonAnalyticsModule,
//        adsModule
    )

private val dataModule
    get() = listOf(
//        databaseModule,
        storageModule,
//        dataRepositoryModule,
//        storeModule
    )

private val featureModules
    get() = listOf(
        featuresUiModule,
        versionModule,
    )

val appModules
    get() = listOf(
        coreModules,
        dataModule,
        featureModules,
    ).flatten()