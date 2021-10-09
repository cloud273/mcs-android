package com.dungnguyen.qdcore.support

class SupportCenter {

    private var index : Int = 0
    private var data : MutableMap<String, Any> = mutableMapOf()

    companion object {
        val instance = SupportCenter()
    }

    fun push(obj: Any) : String {
        val key = "__data__" + index++
        data.put(key, obj)
        return key
    }

    fun<T: Any> pop(key: String): T {
        return data.remove(key) as T
    }

}