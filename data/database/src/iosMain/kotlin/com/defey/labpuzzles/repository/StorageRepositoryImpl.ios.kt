package com.defey.labpuzzles.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.Foundation.NSUserDefaults

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class StorageRepositoryImpl : StorageRepository {

    private val userDefaults = NSUserDefaults.standardUserDefaults()

    private val stringFlows = mutableMapOf<String, MutableStateFlow<String?>>()
    private val intFlows = mutableMapOf<String, MutableStateFlow<Int?>>()
    private val longFlows = mutableMapOf<String, MutableStateFlow<Long?>>()
    private val boolFlows = mutableMapOf<String, MutableStateFlow<Boolean?>>()

    actual override fun setString(key: String, value: String) {
        userDefaults.setObject(value, key)
        stringFlows[key]?.value = value
    }

    actual override fun getString(key: String): String? = userDefaults.stringForKey(key)

    actual override fun getStringFlow(key: String): Flow<String?> = stringFlows.getOrPut(key) {
        MutableStateFlow(getString(key))
    }

    actual override fun setInt(key: String, value: Int) {
        userDefaults.setInteger(value.toLong(), key)
        intFlows[key]?.value = value
    }

    actual override fun getInt(key: String): Int? {
        val value = userDefaults.integerForKey(key).toInt()
        return if (userDefaults.objectForKey(key) != null) value else null
    }

    actual override fun getIntFlow(key: String): Flow<Int?> =
        intFlows.getOrPut(key) { MutableStateFlow(getInt(key)) }

    actual override fun setLong(key: String, value: Long) {
        userDefaults.setInteger(value, key)
        longFlows[key]?.value = value
    }

    actual override fun getLong(key: String): Long? {
        val value = userDefaults.integerForKey(key)
        return if (userDefaults.objectForKey(key) != null) value else null
    }

    actual override fun getLongFlow(key: String): Flow<Long?> =
        longFlows.getOrPut(key) { MutableStateFlow(getLong(key)) }

    actual override fun setBoolean(key: String, value: Boolean) {
        userDefaults.setBool(value, key)
        boolFlows[key]?.value = value
    }

    actual override fun getBoolean(key: String): Boolean = userDefaults.boolForKey(key)

    actual override fun getBooleanFlow(key: String): Flow<Boolean?> =
        boolFlows.getOrPut(key) { MutableStateFlow(getBoolean(key)) }
}