package com.erling.core.load.ffm.api.cpp.struct.value

import com.erling.core.load.ffm.api.cpp.struct.value.TypeEnum.*
import java.lang.foreign.MemorySegment
import java.lang.foreign.ValueLayout

fun getThis(fieldInfo:()-> FieldInfo): Any {
    val fieldInfo = fieldInfo()
    val offset = fieldInfo.offsetMap[fieldInfo.name]!!
    return when(fieldInfo.typeEnum){
        INT,BOOL ->
            fieldInfo.structSegment.get(ValueLayout.JAVA_INT, offset)
        FLOAT ->
            fieldInfo.structSegment.get(ValueLayout.JAVA_FLOAT, offset)
        STRING -> {
            val ptrSeg = fieldInfo.structSegment.get(ValueLayout.ADDRESS, offset)
            ptrSeg.reinterpret(Long.MAX_VALUE).getUtf8String(0)
        }
        ADDRESS ->
            fieldInfo.structSegment.get(ValueLayout.ADDRESS, offset)
    }
}

fun getThisForCArray(structSegment: MemorySegment, offset: Long, maxLen: Int = 100): String {
    val arraySeg = structSegment.asSlice(offset, maxLen.toLong())
    return arraySeg.getUtf8String(0)
}
