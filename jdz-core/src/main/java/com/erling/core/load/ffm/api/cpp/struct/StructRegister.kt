package com.erling.core.load.ffm.api.cpp.struct

import com.erling.core.load.ffm.api.cpp.hook.CreatLayOut
import com.erling.core.load.ffm.api.cpp.hook.Padding
import com.erling.core.load.ffm.api.cpp.hook.SetFieldsOffset
import com.erling.core.load.ffm.api.cpp.struct.field.C_ARRAY
import com.erling.core.load.ffm.api.cpp.struct.field.C_FLOAT
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT
import com.erling.core.load.ffm.api.cpp.struct.field.C_LONG
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER
import java.lang.foreign.Arena
import java.lang.foreign.MemoryLayout
import java.lang.foreign.MemorySegment
import java.lang.foreign.ValueLayout
import kotlin.jvm.java

fun defaultScanStruct(clazz: Class<*>, target: Any){


    val layoutList = mutableListOf<MemoryLayout>()
    clazz.fields.forEach { field ->
        field.isAccessible = true
        val cArrayAnn = field.getAnnotation(CArray::class.java)
        if(cArrayAnn != null && cArrayAnn.len != 0){
            layoutList.add(
                MemoryLayout.sequenceLayout(
                    cArrayAnn.len.toLong(), ValueLayout.JAVA_BYTE
                ).withName(field.name)
            )
        }else{
            when (field.type.simpleName) {
                "MemorySegment" -> {
                    layoutList.add(ValueLayout.ADDRESS.withName(field.name))
                }
                "int" ->
                    layoutList.add(ValueLayout.JAVA_INT.withName(field.name))
                "double" ->
                    layoutList.add(ValueLayout.JAVA_DOUBLE.withName(field.name))
                "float" ->
                    layoutList.add(ValueLayout.JAVA_FLOAT.withName(field.name))
                "long" ->
                    layoutList.add(ValueLayout.JAVA_LONG.withName(field.name))
            }
        }


    }
    val layOut = MemoryLayout.structLayout(*layoutList.toTypedArray())
    val fieldOffsetMap = mutableMapOf<String, Long>()
    clazz.fields.forEach { field ->
       fieldOffsetMap[field.name] = layOut.byteOffset(MemoryLayout.PathElement.groupElement(field.name))
    }

    target.javaClass.methods.forEach {
        val creatLayout = it.getAnnotation(CreatLayOut::class.java)
        if(creatLayout != null){
            it.invoke(target,layOut)
        }
        val setOffset = it.getAnnotation(SetFieldsOffset::class.java)
        if(setOffset != null){
            it.invoke(target,fieldOffsetMap)
        }
    }
//    println(layoutList.joinToString(","))

}

fun registerStruct(clazz: Class<*>, target: Any){
    val layoutList = mutableListOf<MemoryLayout>()
    clazz.fields.forEach { field ->
        field.isAccessible = true
            when (field.type) {
                C_POINTER::class.java -> {

                    layoutList.add(ValueLayout.ADDRESS.withName(field.name))
                    val padding = field.getAnnotation(Padding::class.java)
                    if(padding != null){
                        layoutList.add(MemoryLayout.paddingLayout(padding.value.toLong()))
                    }
                }
                C_FLOAT::class.java ->{
                    layoutList.add(ValueLayout.JAVA_FLOAT.withName(field.name))
                    val padding = field.getAnnotation(Padding::class.java)
                    if(padding != null){
                        layoutList.add(MemoryLayout.paddingLayout(padding.value.toLong()))
                    }
                }
                C_INT::class.java ->{
                    layoutList.add(ValueLayout.JAVA_INT.withName(field.name))
                    val padding = field.getAnnotation(Padding::class.java)
                    if(padding != null){
                        layoutList.add(MemoryLayout.paddingLayout(padding.value.toLong()))
                    }
                }

                C_LONG::class.java ->{
                    layoutList.add(ValueLayout.JAVA_LONG.withName(field.name))
                }

                C_ARRAY::class.java ->{
                    val cArrayAnn = field.getAnnotation(CArray::class.java)
                    if(cArrayAnn != null && cArrayAnn.len != 0){
                        layoutList.add(
                            MemoryLayout.sequenceLayout(
                                cArrayAnn.len.toLong(), ValueLayout.JAVA_BYTE
                            ).withName(field.name)
                        )
                    }
                    val padding = field.getAnnotation(Padding::class.java)
                    if(padding != null){
                        layoutList.add(MemoryLayout.paddingLayout(padding.value.toLong()))
                    }
                }

            }

    }

    val layOut = MemoryLayout.structLayout(*layoutList.toTypedArray())
    val fieldOffsetMap = mutableMapOf<String, Long>()
    clazz.fields.forEach { field ->
        fieldOffsetMap[field.name] = layOut.byteOffset(MemoryLayout.PathElement.groupElement(field.name))
    }

    target.javaClass.methods.forEach {
        val creatLayout = it.getAnnotation(CreatLayOut::class.java)
        if(creatLayout != null){
            it.invoke(target,layOut)
        }
        val setOffset = it.getAnnotation(SetFieldsOffset::class.java)
        if(setOffset != null){
            it.invoke(target,fieldOffsetMap)
        }
    }
}


fun initFields(target: Any){
    var memorySegment : MemorySegment? = null
    var fieldOffsetMap: MutableMap<*, *> = mutableMapOf<String, Long>()
    var arena : Arena? = null
    target.javaClass.methods.forEach {
        it.isAccessible = true
        if(it.name.startsWith("getMemorySegment")){
           memorySegment = it.invoke(target) as MemorySegment
        }
        if(it.name.startsWith("getFieldOffsetMap")){
          fieldOffsetMap = it.invoke(target) as MutableMap<*, *>
        }
        if(it.name.startsWith("getArena")){
            arena = it.invoke(target) as Arena
        }
    }

    if(memorySegment != null){
        target.javaClass.fields.forEach { field ->
            field.isAccessible = true
            when (field.type) {

                C_POINTER::class.java -> {
                    val constructor = C_POINTER::class.java.getConstructor(
                        MemorySegment::class.java,
                        Long::class.javaPrimitiveType,
                        Arena::class.java)
                    val instance = constructor.newInstance(
                        memorySegment, fieldOffsetMap[field.name], arena
                    )
                    field.set(target, instance)
                }

                C_INT::class.java -> {
                    val constructor = C_INT::class.java.getConstructor(
                        MemorySegment::class.java,
                        Long::class.javaPrimitiveType,
                        Arena::class.java
                    )
                    val instance = constructor.newInstance(
                        memorySegment, fieldOffsetMap[field.name], arena)
                    field.set(target, instance)
                }

                C_FLOAT::class.java -> {
                    val constructor = C_FLOAT::class.java.getConstructor(
                        MemorySegment::class.java,
                        Long::class.javaPrimitiveType,
                        Arena::class.java
                    )
                    val instance = constructor.newInstance(
                        memorySegment, fieldOffsetMap[field.name], arena
                    )
                    field.set(target, instance)
                }

                C_LONG::class.java -> {
                    val constructor = C_LONG::class.java.getConstructor(
                        MemorySegment::class.java,
                        Long::class.javaPrimitiveType,
                        Arena::class.java
                    )
                    val instance = constructor.newInstance(
                        memorySegment, fieldOffsetMap[field.name], arena
                    )
                    field.set(target, instance)
                }

                C_ARRAY::class.java -> {
                    val cArrayAnn = field.getAnnotation(CArray::class.java)
                    if(cArrayAnn != null && cArrayAnn.len != 0){
                        val constructor = C_ARRAY::class.java.getConstructor(
                            MemorySegment::class.java,
                            Long::class.javaPrimitiveType,
                            Arena::class.java,
                            Long::class.javaPrimitiveType
                        )
                        val instance = constructor.newInstance(
                            memorySegment, fieldOffsetMap[field.name],
                            arena,
                            cArrayAnn.len.toLong()
                        )
                        field.set(target, instance)
                    }
                }

            }
        }
    }
}


