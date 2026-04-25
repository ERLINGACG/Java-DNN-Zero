package com.erling.plugins.json.v2

import com.sun.jna.Memory
import com.sun.jna.Pointer
import com.sun.jna.Structure

@Suppress("PropertyName")
class JsonData : Structure() {

    var jsonPluginsV2: JsonPluginsV2? = null   // 非 JNA 字段，仅用于业务逻辑

    @JvmField var json_root: Pointer? = null   // 对应 unique_ptr 内部的裸指针
    @JvmField var json_now: Pointer? = null
    @JvmField var now_level: Int = 0

    override fun getFieldOrder(): List<String> {
        return listOf("json_root", "json_now", "now_level")
    }

    fun withJsonPluginsV2(jsonPluginsV2: JsonPluginsV2?): JsonData {
        this.jsonPluginsV2 = jsonPluginsV2
        return this
    }

    fun build(json: String): JsonData {

        val utf8Bytes = json.toByteArray(Charsets.UTF_8)   // 1. 将Java字符串转为UTF-8字节数组
        val buffer = Memory((utf8Bytes.size + 1).toLong())  // 2. 分配本地内存，多一个字节给结尾的 '\0'

        buffer.write(0, utf8Bytes, 0, utf8Bytes.size)  // 3. 写入字节数组
        buffer.setByte(utf8Bytes.size.toLong(), 0.toByte())  // 4. 设置结尾 '\0'

        jsonPluginsV2?.getJsonPluginsV2Inf()?.BuildJsonData(this, buffer)
        return this
    }

    fun get(arg: String) : JsonData{

        val utf8Bytes = arg.toByteArray(Charsets.UTF_8)   // 1. 将Java字符串转为UTF-8字节数组
        val buffer = Memory((utf8Bytes.size + 1).toLong())  // 2. 分配本地内存，多一个字节给结尾的 '\0'

        buffer.write(0, utf8Bytes, 0, utf8Bytes.size)  // 3. 写入字节数组
        buffer.setByte(utf8Bytes.size.toLong(), 0.toByte())  // 4. 设置结尾 '\0'

        jsonPluginsV2?.getJsonPluginsV2Inf()?.PostToKey(this, buffer, 1)
        return this
    }

    fun get(arg: Int) : JsonData{
        val utf8Bytes = arg.toString().toByteArray(Charsets.UTF_8)   // 1. 将Java字符串转为UTF-8字节数组
        val buffer = Memory((utf8Bytes.size + 1).toLong())  // 2. 分配本地内存，多一个字节给结尾的 '\0'

        buffer.write(0, utf8Bytes, 0, utf8Bytes.size)  // 3. 写入字节数组
        buffer.setByte(utf8Bytes.size.toLong(), 0.toByte())  // 4. 设置结尾 '\0'

        jsonPluginsV2?.getJsonPluginsV2Inf()?.PostToKey(this, buffer, 0)
        return this
    }

    fun at(): JsonNodeInfo {
        val jsonNodeInfo = JsonNodeInfo()
        jsonPluginsV2?.getJsonPluginsV2Inf()?.GetValue(this, jsonNodeInfo)
        return jsonNodeInfo
    }





}