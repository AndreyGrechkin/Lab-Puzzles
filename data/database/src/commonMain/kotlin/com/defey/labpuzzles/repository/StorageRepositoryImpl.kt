package com.defey.labpuzzles.repository

import kotlinx.coroutines.flow.Flow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class StorageRepositoryImpl : StorageRepository {
    override fun setString(key: String, value: String)
    override fun getString(key: String): String?
    override fun getStringFlow(key: String): Flow<String?>
    override fun setInt(key: String, value: Int)
    override fun getInt(key: String): Int?
    override fun getIntFlow(key: String): Flow<Int?>
    override fun setLong(key: String, value: Long)
    override fun getLong(key: String): Long?
    override fun getLongFlow(key: String): Flow<Long?>
    override fun setBoolean(key: String, value: Boolean)
    override fun getBoolean(key: String): Boolean
    override fun getBooleanFlow(key: String): Flow<Boolean?>
}