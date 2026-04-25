package com.erling.plugins.json.v2

import com.sun.jna.Pointer
import com.sun.jna.Structure


//struct JsonNodeInfo
//{
//    std::unique_ptr<char[]> key{};
//    int key_len{};
//
//    std::unique_ptr<char[]> value{};
//    int value_len{};
//
//    std::unique_ptr<char[]> type{};
//    int type_len{};
//
//    int level = 0;
//
//};
@Suppress("PropertyName")
class JsonNodeInfo : Structure() {
    @JvmField var key: Pointer? = null
    @JvmField var key_len: Int = 0
    @JvmField var value: Pointer? = null
    @JvmField var value_len: Int = 0
    @JvmField var type: Pointer? = null
    @JvmField var type_len: Int = 0
    @JvmField var level: Int = 0

    override fun getFieldOrder(): List<String> {
        return listOf(
            "key", "key_len",
            "value", "value_len",
            "type", "type_len",
            "level"
        )
    }

    fun Pointer.readUtf8String(byteLength: Int): String {
        val bytes = getByteArray(0, byteLength)
        return String(bytes, Charsets.UTF_8)
    }

    fun getKey(): String {
        return key?.readUtf8String(key_len) ?: ""
    }

    fun getValue(): String {
        return value?.readUtf8String(value_len) ?: ""
    }

    fun getType(): String {
        return type?.readUtf8String(type_len) ?: ""
    }

    fun getLevel(): Int {
        return level
    }
}