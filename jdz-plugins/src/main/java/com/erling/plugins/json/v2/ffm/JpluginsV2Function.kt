package com.erling.plugins.json.v2.ffm

import java.lang.foreign.FunctionDescriptor
import java.lang.foreign.ValueLayout

var  build_descriptor: FunctionDescriptor? = FunctionDescriptor.of(
    ValueLayout.JAVA_INT, // 返回值 int,
    ValueLayout.ADDRESS, // 参数1: JsonData*,
    ValueLayout.ADDRESS // 参数2: const char*
)