package com.defey.labpuzzles

import com.defey.labpuzzles.models.AppInfo
import com.defey.labpuzzles.repository.AppInfoProvider
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSBundle

actual val versionModule: Module
    get() = module {
        single<AppInfoProvider> {
            object : AppInfoProvider {
                override val appInfo: AppInfo
                    get() = AppInfo(
                        versionName = NSBundle.mainBundle.infoDictionary
                            ?.get("CFBundleShortVersionString") as? String ?: "1.0",
                        versionCode = (NSBundle.mainBundle.infoDictionary
                            ?.get("CFBundleVersion") as? String)?.toIntOrNull() ?: 1
                    )
            }
        }
    }