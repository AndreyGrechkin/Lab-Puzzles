package com.defey.labpuzzles

import com.defey.labpuzzles.models.AppInfo
import com.defey.labpuzzles.repository.AppInfoProvider
import org.koin.core.module.Module
import org.koin.dsl.module

actual val versionModule: Module
    get() = module {
        single<AppInfoProvider> {
            object : AppInfoProvider {
                override val appInfo: AppInfo
                    get() = AppInfo(
                        versionName = BuildConfig.VERSION_NAME,
                        versionCode = BuildConfig.VERSION_CODE
                    )
            }
        }
    }