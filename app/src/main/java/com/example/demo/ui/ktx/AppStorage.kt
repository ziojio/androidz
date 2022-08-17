package com.example.demo.ui.ktx

import com.tencent.mmkv.MMKV

object AppStorage {
    @JvmStatic
    val instance: MMKV by lazy {
        MMKV.mmkvWithID("AppStorage")
    }

    @JvmStatic
    fun keys(): Array<String> {
        return instance.allKeys() ?: emptyArray()
    }

    @JvmStatic
    fun contains(key: String): Boolean {
        return instance.containsKey(key)
    }

    @JvmStatic
    fun remove(key: String) {
        instance.removeValueForKey(key)
    }

    @JvmStatic
    fun clear() {
        instance.clearAll()
    }

    @JvmStatic
    fun sync() {
        instance.sync()
    }

    @JvmStatic
    fun set(key: String, value: Int): Boolean {
        return instance.encode(key, value)
    }

    @JvmStatic
    fun set(key: String, value: Long): Boolean {
        return instance.encode(key, value)
    }

    @JvmStatic
    fun set(key: String, value: Float): Boolean {
        return instance.encode(key, value)
    }

    @JvmStatic
    fun set(key: String, value: Double): Boolean {
        return instance.encode(key, value)
    }

    @JvmStatic
    fun set(key: String, value: Boolean): Boolean {
        return instance.encode(key, value)
    }

    @JvmStatic
    fun set(key: String, value: String?): Boolean {
        return instance.encode(key, value)
    }

    @JvmOverloads
    @JvmStatic
    fun getString(key: String, defValue: String? = null): String? {
        return instance.getString(key, defValue)
    }

    @JvmOverloads
    @JvmStatic
    fun getInt(key: String, defValue: Int = 0): Int {
        return instance.getInt(key, defValue)
    }

    @JvmOverloads
    @JvmStatic
    fun getLong(key: String, defValue: Long = 0): Long {
        return instance.getLong(key, defValue)
    }

    @JvmOverloads
    @JvmStatic
    fun getFloat(key: String, defValue: Float = 0f): Float {
        return instance.getFloat(key, defValue)
    }

    @JvmOverloads
    @JvmStatic
    fun getDouble(key: String, defValue: Double = 0.0): Double {
        return instance.decodeDouble(key, defValue)
    }

    @JvmOverloads
    @JvmStatic
    fun getBoolean(key: String, defValue: Boolean = false): Boolean {
        return instance.getBoolean(key, defValue)
    }
}
