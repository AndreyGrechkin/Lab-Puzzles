package com.defey.labpuzzles.managers.localization

import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages

internal class IosLocalization : Localization {
    override  fun applyLanguage(iso: String) {
        NSUserDefaults.standardUserDefaults.setObject(
            arrayListOf(iso), "AppleLanguages"
        )
    }

    override fun getSystemLanguageCode(): String {
        return NSLocale.preferredLanguages.getOrNull(0)?.toString() ?: "en"
    }
}