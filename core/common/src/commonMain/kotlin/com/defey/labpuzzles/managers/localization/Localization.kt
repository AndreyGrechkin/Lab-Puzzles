package com.defey.labpuzzles.managers.localization

interface Localization {
    fun applyLanguage(iso: String)
    fun getSystemLanguageCode(): String
}