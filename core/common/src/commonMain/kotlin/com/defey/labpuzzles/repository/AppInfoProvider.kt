package com.defey.labpuzzles.repository

import com.defey.labpuzzles.models.AppInfo

interface AppInfoProvider {
    val appInfo: AppInfo
}