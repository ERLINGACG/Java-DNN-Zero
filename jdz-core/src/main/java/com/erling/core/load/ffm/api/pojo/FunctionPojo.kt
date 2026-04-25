package com.erling.core.load.ffm.api.pojo

import kotlinx.serialization.Serializable

//{
//    "function": {
//    "name": "add",
//    "args": [
//    {"name": "a", "type": "int"},
//    {"name": "b", "type": "int"}
//    ],
//    "returnType": "int"
//}
//}

@Serializable
data class FunctionArray(
    val functions: List<FunctionPojo>
)

@Serializable
data class FunctionPojo(
    val name: String,
    val args: List<String>,
    val returnType: String
)

