package com.jxgis.datastoredemo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException

/**
 * Created by YB on 2021/3/4
 */
class DsUtil private constructor(context: Context, name: String) {
    val dataStore: DataStore<Preferences> = context.createDataStore(name)

    ///////////////////////////////////////////////////////////////////////////
    // 异步方法
    ///////////////////////////////////////////////////////////////////////////
    fun getString(key: String, default: String? = null): Flow<String?> {
        return dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[stringPreferencesKey(key)] ?: default
            }
    }

    fun getInt(key: String, default: Int? = null): Flow<Int?> {
        return dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[intPreferencesKey(key)] ?: default
            }
    }

    fun getLong(key: String, default: Long? = null): Flow<Long?> {
        return dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[longPreferencesKey(key)] ?: default
            }
    }

    fun getFloat(key: String, default: Float? = null): Flow<Float?> {
        return dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[floatPreferencesKey(key)] ?: default
            }
    }

    fun getDouble(key: String, default: Double? = null): Flow<Double?> {
        return dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[doublePreferencesKey(key)] ?: default
            }
    }

    fun getBoolean(key: String, default: Boolean? = null): Flow<Boolean?> {
        return dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[booleanPreferencesKey(key)] ?: default
            }
    }

    fun getStringSet(key: String, default: Set<String>? = null): Flow<Set<String>?> {
        return dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[stringSetPreferencesKey(key)] ?: default
            }
    }

    suspend fun putString(key: String, value: String) {
        dataStore.edit {
            val preKey = stringPreferencesKey(key)
            it[preKey] = value
        }
    }

    suspend fun putInt(key: String, value: Int) {
        dataStore.edit {
            val preKey = intPreferencesKey(key)
            it[preKey] = value
        }
    }

    suspend fun putLong(key: String, value: Long) {
        dataStore.edit {
            val preKey = longPreferencesKey(key)
            it[preKey] = value
        }
    }

    suspend fun putFloat(key: String, value: Float) {
        dataStore.edit {
            val preKey = floatPreferencesKey(key)
            it[preKey] = value
        }
    }

    suspend fun putDouble(key: String, value: Double) {
        dataStore.edit {
            val preKey = doublePreferencesKey(key)
            it[preKey] = value
        }
    }

    suspend fun putBoolean(key: String, value: Boolean) {
        dataStore.edit {
            val preKey = booleanPreferencesKey(key)
            it[preKey] = value
        }
    }

    suspend fun putStringSet(key: String, value: Set<String>) {
        dataStore.edit {
            val preKey = stringSetPreferencesKey(key)
            it[preKey] = value
        }
    }

    suspend fun clearDataStore() {
        dataStore.edit {
            it.clear()
        }
    }

    suspend fun clearDataStore(key: String) {
        dataStore.edit {
            val preKey = stringSetPreferencesKey(key)
            it.remove(preKey)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 同步方法
    ///////////////////////////////////////////////////////////////////////////
    fun getStringSync(key: String, default: String? = null) = runBlocking { getString(key, default).first() }

    fun getIntSync(key: String, default: Int? = null) = runBlocking { getInt(key, default).first() }

    fun getLongSync(key: String, default: Long? = null) = runBlocking { getLong(key, default).first() }

    fun getFloatSync(key: String, default: Float? = null) = runBlocking { getFloat(key, default).first() }

    fun getDoubleSync(key: String, default: Double? = null) = runBlocking { getDouble(key, default).first() }

    fun getBooleanSync(key: String, default: Boolean? = null) = runBlocking { getBoolean(key, default).first() }

    fun getStringSetSync(key: String, default: Set<String>? = null) = runBlocking { getStringSet(key, default).first() }

    fun putStringSync(key: String, value: String) = runBlocking { putString(key, value) }

    fun putIntSync(key: String, value: Int) = runBlocking { putInt(key, value) }

    fun putLongSync(key: String, value: Long) = runBlocking { putLong(key, value) }

    fun putFloatSync(key: String, value: Float) = runBlocking { putFloat(key, value) }

    fun putDoubleSync(key: String, value: Double) = runBlocking { putDouble(key, value) }

    fun putBooleanSync(key: String, value: Boolean) = runBlocking { putBoolean(key, value) }

    fun putStringSetSync(key: String, value: Set<String>) = runBlocking { putStringSet(key, value) }

    fun clearDataStoreSync() = runBlocking { clearDataStore() }

    fun clearDataStoreSyncSync(key: String) = runBlocking { clearDataStore(key) }

    companion object {
        private var ds: HashMap<String, DsUtil> = hashMapOf()
        fun getInstance(context: Context, name: String = "UtilDs"): DsUtil {
            return if (ds.containsKey(name)) {
                ds[name]!!
            } else {
                val dsUtil = DsUtil(context, name)
                ds[name] = dsUtil
                dsUtil
            }
        }
    }
}