package com.defey.labpuzzles.managers.localization

enum class AppLanguage(val iso: String, val displayName: String) {
    SYSTEM("system", "System"),
    ENGLISH("en", "English"),
    RUSSIAN("ru", "Русский");

    companion object {
        fun fromLanguageCode(code: String): AppLanguage {
            return when (code) {
                "en" -> ENGLISH
                "ru" -> RUSSIAN
                "system" -> SYSTEM
                else -> SYSTEM
            }
        }
    }
}