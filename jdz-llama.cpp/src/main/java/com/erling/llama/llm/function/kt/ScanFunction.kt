package com.erling.llama.llm.function.kt

import com.erling.llama.llm.function.ann.FuncParam
import com.erling.llama.llm.function.ann.FuncTools
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.reflect.Method
import kotlin.jvm.javaClass


fun scanFunction() {

}
// [ToolSpecification {
//     name = "getWeather",
//     description = "返回指定城市的当前天气情况",
//     parameters = JsonObjectSchema {
//         description = null,
//         properties = {
//             arg0 = JsonStringSchema {
//                 description = "需要查询天气的城市名称，例如：北京、上海"
//             },
//             arg1 = JsonStringSchema {
//                 description = "温度单位，可选值为 'Celsius' 或 'Fahrenheit'，默认为 'Celsius'"
//             }
//         },
//         required = [arg0, arg1],
//         additionalProperties = null,
//         definitions = {}
//     },
//     metadata = {}
// }]-> to:
//[
//    {
//        "name": "getWeather",
//        "description": "返回指定城市的当前天气情况",
//        "parameters": {
//        "type": "object",
//        "properties": {
//        "arg0": {
//        "type": "string",
//        "description": "需要查询天气的城市名称，例如：北京、上海"
//    },
//        "arg1": {
//        "type": "string",
//        "description": "温度单位，可选值为 'Celsius' 或 'Fahrenheit'，默认为 'Celsius'"
//    }
//    },
//        "required": ["arg0", "arg1"]
//    }
//    }
//]

private val json1 = Json { prettyPrint = true }

fun scanFunctionGetInfo(funcObjects: () -> Any): Pair<String, FunctionObject> {

    val instance = funcObjects.invoke()
    val methods = instance.javaClass.methods
    val methodArray = mutableListOf<Method>()
    val methodInfos = methods.mapNotNull { method ->
        method.getAnnotation(FuncTools::class.java)?.let { annotation ->
            methodArray.add(method)
            MethodInfo(
                method.name,
                annotation.description,
                parameters = JsonObjectSchema(
                    type = "object",
                    required = method.parameters.map { it.name },
                    properties = method.parameters.associate { param ->
                        param.name to ParameterInfo(
                            description = param.getAnnotation(FuncParam::class.java)?.description ?: "",
                            type = param.type.simpleName
                        )
                    }
                )
            )
        }
    }
    val json = json1.encodeToString(methodInfos)
    return Pair(json, FunctionObject(instance,methodArray))
}