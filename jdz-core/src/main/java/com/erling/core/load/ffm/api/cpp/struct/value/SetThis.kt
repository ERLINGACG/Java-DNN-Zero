package com.erling.core.load.ffm.api.cpp.struct.value

import com.erling.core.load.ffm.api.cpp.struct.value.TypeEnum.*
import java.lang.foreign.Arena
import java.lang.foreign.MemorySegment
import java.lang.foreign.ValueLayout


data class FieldInfo(
    val typeEnum: TypeEnum,
    val offsetMap: Map<String, Long>,
    val structSegment: MemorySegment,
    val name: String,
    val value: Any?
)

fun setThis(arena: Arena, fieldInfo: ()->FieldInfo)
{
    val fieldInfo = fieldInfo()
    val offset = fieldInfo.offsetMap[fieldInfo.name] ?: return
    when(fieldInfo.typeEnum) {
        INT, BOOL ->
            fieldInfo.structSegment.set(ValueLayout.JAVA_INT, offset, fieldInfo.value as Int)
        FLOAT ->
            fieldInfo.structSegment.set(ValueLayout.JAVA_FLOAT, offset, fieldInfo.value as Float)
        ADDRESS, STRING ->
            fieldInfo.structSegment.set(ValueLayout.ADDRESS, offset, fieldInfo.value as MemorySegment)
    }
}

fun setThisForCArray(structSegment: MemorySegment,
                     offset: Long,
                     value: String,
                     maxLen: Int = 100
){
    val arraySeg = structSegment.asSlice(offset, maxLen.toLong())
    arraySeg.fill(0)
    arraySeg.setUtf8String(0, value)

}
