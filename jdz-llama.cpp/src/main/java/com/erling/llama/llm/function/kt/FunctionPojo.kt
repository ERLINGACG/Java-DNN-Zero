package com.erling.llama.llm.function.kt

import com.erling.llama.llm.backend.LlamaCallBack
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import lombok.Getter
import java.lang.reflect.Method
import java.util.function.Supplier

// [
//     {
//         "name": "getWeather",
//         "description": "返回指定城市的当前天气情况",
//         "parameters": {
//             "type": "object",
//             "properties": {
//                 "arg0": {
//                     "type": "string",
//                     "description": "需要查询天气的城市名称，例如：北京、上海"
//                 },
//                 "arg1": {
//                     "type": "string",
//                     "description": "温度单位，可选值为 'Celsius' 或 'Fahrenheit'，默认为 'Celsius'"
//                 }
//             },
//             "required": ["arg0", "arg1"]
//         }
//     }
// ]
@Serializable
data class MethodInfo(
    val name: String,
    val description: String,
    val parameters: JsonObjectSchema
)

@Serializable
data class JsonObjectSchema(
    val type: String,
    val properties: Map<String, ParameterInfo>,
    val required: List<String>
)

@Serializable
data class ParameterInfo(

    val description: String,
    val type: String,

)
@Serializable
data class InvokeObject(
    val name: String,
    val arguments: JsonObject  // 注意：字段名需与 JSON 一致（arguments）
)

@Serializable
data class StepInvokeObject(
    val name: String,
    val arguments: JsonObject,  // 注意：字段名需与 JSON 一致（arguments）
    var isNext: Boolean)


@Getter
data class FunctionObject(
    val objects: Any,
    val methods: List<Method>
)

@Getter
data class UserToolRT<C,P>(
    var name: String,
    var system: String,
    var prompt: String,
    var rtParam: Supplier<C>,
    var ctx:     Supplier<P>,
    var recCallBack: LlamaCallBack
)


@Serializable
data class FunctionResult(
    val result: JsonObject
)
