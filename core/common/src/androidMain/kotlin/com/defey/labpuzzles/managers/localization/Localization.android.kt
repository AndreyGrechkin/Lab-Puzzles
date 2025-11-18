package com.defey.labpuzzles.managers.localization

import android.content.Context
import android.content.res.Resources
import android.os.LocaleList
import androidx.core.os.ConfigurationCompat
import java.util.Locale

internal class AndroidLocalization(
    private val context: Context
) : Localization {
    override fun applyLanguage(iso: String) {
        val locale = Locale.forLanguageTag(iso)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocales(LocaleList(locale))
        context.createConfigurationContext(config)
    }

    override fun getSystemLanguageCode(): String {
        val locale = ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)
            ?: Locale.getDefault()
        return  locale.language
    }
}