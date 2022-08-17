package com.example.demo.ui.ktx

object AppCache {
    @JvmStatic
    val instance: LinkedHashMap<String, Any> by lazy {
        LinkedHashMap()
    }

    @JvmStatic
    fun keys(): Set<String> {
        return instance.keys
    }

    @JvmStatic
    fun contains(key: String): Boolean {
        return instance.containsKey(key)
    }

    @JvmStatic
    fun remove(key: String) {
        instance.remove(key)
    }

    @JvmStatic
    fun clear() {
        instance.clear()
    }

    @JvmStatic
    fun set(key: String, obj: Any) {
        instance[key] = obj
    }

    @JvmStatic
    fun get(key: String): Any? {
        return instance[key]
    }
}
