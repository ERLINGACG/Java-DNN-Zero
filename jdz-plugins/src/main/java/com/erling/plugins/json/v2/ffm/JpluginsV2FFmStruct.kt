package com.erling.plugins.json.v2.ffm

import java.lang.foreign.MemoryLayout
import java.lang.foreign.StructLayout
import java.lang.foreign.ValueLayout


class JsonData{
    val JSON_DATA_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.ADDRESS.withName("json_root"),   // 偏移 0
                    ValueLayout.ADDRESS.withName("json_now"),    // 偏移 8
                    ValueLayout.ADDRESS.withName("json_value"),  // 偏移 16
                    ValueLayout.JAVA_INT.withName("now_level"),  // 偏移 24
                    MemoryLayout.paddingLayout(4)      // 偏移 28，对齐到 32
    ).withName("JsonData")

    fun get(): StructLayout {
        return JSON_DATA_LAYOUT
    }
}
