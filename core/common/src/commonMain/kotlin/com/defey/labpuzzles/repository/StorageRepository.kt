package com.defey.labpuzzles.repository

import kotlinx.coroutines.flow.Flow

interface StorageRepository {

    fun setString(key: String, value: String)
    fun getString(key: String): String?
    fun getStringFlow(key: String): Flow<String?>

    fun setInt(key: String, value: Int)
    fun getInt(key: String): Int?
    fun getIntFlow(key: String): Flow<Int?>

    fun setLong(key: String, value: Long)
    fun getLong(key: String): Long?
    fun getLongFlow(key: String): Flow<Long?>

    fun setBoolean(key: String, value: Boolean)
    fun getBoolean(key: String): Boolean
    fun getBooleanFlow(key: String): Flow<Boolean?>
}