package com.erling.llama.llm.function.kt

import com.erling.llama.llm.function.FunctionEngine
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.double
import kotlinx.serialization.json.float
import kotlinx.serialization.json.int
import kotlinx.serialization.json.long
import kotlin.collections.iterator

fun invoke(functionEngine: FunctionEngine, json: String): String {
    val obj = Json.decodeFromString<InvokeObject>(json)

    // 收集原始参数（未转换）
    val objs = mutableListOf<Any>()
    for ((_, value) in obj.arguments) {
        objs.add(value)
    }

    functionEngine.functionInfo.second.methods.forEach { method ->
        if (method.name == obj.name) {
            println("try invoke: ${method.name}")
            method.isAccessible = true

            // 根据目标方法的参数类型，动态转换参数
            val convertedArgs = method.parameterTypes.mapIndexed { i, type ->
                val raw = objs.getOrNull(i)  // 使用 objs，不是 args
                when (type) {
                    Int::class.javaPrimitiveType, Int::class.java ->
                        (raw as? JsonPrimitive)?.int ?: raw as? Int

                    Long::class.javaPrimitiveType, Long::class.java ->
                        (raw as? JsonPrimitive)?.long ?: raw as? Long

                    Float::class.javaPrimitiveType, Float::class.java ->
                        (raw as? JsonPrimitive)?.float ?: raw as? Float

                    Double::class.javaPrimitiveType, Double::class.java ->
                        (raw as? JsonPrimitive)?.double ?: raw as? Double

                    String::class.java ->
                        raw.toString()
                    else ->
                        raw
                }
            }.toTypedArray()

            var result: Any?
            try {
                result = method.invoke(
                    functionEngine.functionInfo.second.objects,
                    *convertedArgs   // 这里用 convertedArgs，不是 objs
                )
            } catch (e: Exception) {
                result = e.message
                println(e)
            }
            if(result==null) result = "result is void"
            // 使用转换后的参数调用


            return Json.encodeToString(FunctionResult(
                JsonObject(mapOf(
                            "useTool" to    JsonPrimitive(method.name),
                            "result" to JsonPrimitive(result.toString())
                        )
                    )
                )
            )
        }
    }
    return Json.encodeToString(
        FunctionResult(
            JsonObject(mapOf(
                            "useTool" to    JsonPrimitive(""),
                            "result" to JsonPrimitive("not use tools")))
        )
    )
}

