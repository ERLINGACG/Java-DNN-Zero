package com.erling.core.load.ffm.api.uitls

import com.erling.core.load.ffm.api.pojo.FunctionArray
import kotlinx.serialization.json.Json
import kotlin.jvm.java

fun  readJson(json: String): FunctionArray {
    return Json.decodeFromString<FunctionArray>(json)
}