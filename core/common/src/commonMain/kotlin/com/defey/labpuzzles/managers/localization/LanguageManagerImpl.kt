package com.defey.labpuzzles.managers.localization

import com.defey.labpuzzles.repository.StorageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LanguageManagerImpl(
    private val localization: Localization,
    private val repository: StorageRepository,
) : LanguageManager {

    private val _languageFlow = MutableStateFlow(currentLanguage)
    override val currentLanguage: AppLanguage
        get() = AppLanguage.fromLanguageCode(
            repository.getString(LANGUAGE_KEY) ?: AppLanguage.SYSTEM.iso
        )

    override val languageFlow: StateFlow<AppLanguage>
        get() = _languageFlow

    override fun setLanguage(language: AppLanguage) {
        repository.setString(LANGUAGE_KEY, language.iso)
        _languageFlow.update { language }
        if (language != AppLanguage.SYSTEM) {
            localization.applyLanguage(language.iso)
        } else {
            localization.applyLanguage(localization.getSystemLanguageCode())
        }
    }

    init {
        when (currentLanguage) {
            AppLanguage.SYSTEM -> localization.applyLanguage(localization.getSystemLanguageCode())
            else -> localization.applyLanguage(currentLanguage.iso)
        }
    }

    override fun getAvailableLanguages(): List<AppLanguage> {
        return AppLanguage.entries
    }

    companion object {
        private const val LANGUAGE_KEY = "language_key"
    }
}