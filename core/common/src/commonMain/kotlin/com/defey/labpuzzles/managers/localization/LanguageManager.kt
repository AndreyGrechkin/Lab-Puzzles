package com.defey.labpuzzles.managers.localization

import kotlinx.coroutines.flow.StateFlow

interface LanguageManager {
    val currentLanguage: AppLanguage
    val languageFlow: StateFlow<AppLanguage>
    fun setLanguage(language: AppLanguage)
    fun getAvailableLanguages(): List<AppLanguage>
}