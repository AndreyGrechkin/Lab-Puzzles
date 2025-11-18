package com.defey.labpuzzles.repository

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class StorageRepositoryImpl(
    private val context: Context,
) : StorageRepository {

    private val prefs by lazy {
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    private val stringFlows = mutableMapOf<String, MutableStateFlow<String?>>()
    private val intFlows = mutableMapOf<String, MutableStateFlow<Int?>>()
    private val longFlows = mutableMapOf<String, MutableStateFlow<Long?>>()
    private val boolFlows = mutableMapOf<String, MutableStateFlow<Boolean?>>()

    actual override fun setString(key: String, value: String) {
        prefs.edit { putString(key, value) }
        stringFlows[key]?.value = value
    }

    actual override fun getString(key: String): String? = prefs.getString(key, null)

    actual override fun getStringFlow(key: String): Flow<String?> = stringFlows.getOrPut(key) {
        MutableStateFlow(getString(key))
    }

    actual override fun setInt(key: String, value: Int) {
        prefs.edit { putInt(key, value) }
        intFlows[key]?.value = value
    }

    actual override fun getInt(key: String): Int? =
        if (prefs.contains(key)) prefs.getInt(key, 0) else null

    actual override fun getIntFlow(key: String): Flow<Int?> =
        intFlows.getOrPut(key) { MutableStateFlow(getInt(key)) }

    actual override fun setLong(key: String, value: Long) {
        prefs.edit { putLong(key, value) }
        longFlows[key]?.value = value
    }

    actual override fun getLong(key: String): Long? =
        if (prefs.contains(key)) prefs.getLong(key, 0L) else null

    actual override fun getLongFlow(key: String): Flow<Long?> =
        longFlows.getOrPut(key) { MutableStateFlow(getLong(key)) }

    actual override fun setBoolean(key: String, value: Boolean) {
        prefs.edit { putBoolean(key, value) }
        boolFlows[key]?.value = value
    }

    actual override fun getBoolean(key: String): Boolean = prefs.getBoolean(key, false)

    actual override fun getBooleanFlow(key: String): Flow<Boolean?> =
        boolFlows.getOrPut(key) { MutableStateFlow(getBoolean(key)) }
}