package com.erling.ort.llm.model.shape

import com.erling.ort.core.shape.OnnxMirrorShape
import com.erling.ort.core.struct.OnnxRtInput
import com.erling.ort.core.struct.OnnxRtShape
import com.erling.ort.llm.exce.OrtLLmShapeIsUnKnow
import com.erling.ort.llm.model.OnnxLLmModel
import java.lang.foreign.Arena
import kotlin.collections.toLongArray

fun buildShape(onnxLLmModel: OnnxLLmModel, arena: Arena, onnxRtInput: OnnxRtInput, inputShapes: List<OnnxMirrorShape<*>>) {
    inputShapes.forEach { inputDesc ->
        // 1. 将多种数据结构统一转换为 LongArray?（null 表示空数据）
        val longArray = when (val data = inputDesc.data) {
            is LongArray -> data
            is IntArray -> data.map { it.toLong() }.toLongArray()
            is ShortArray -> data.map { it.toLong() }.toLongArray()
            is ByteArray -> data.map { it.toLong() }.toLongArray()
            is Array<*> -> data.mapNotNull { (it as? Number)?.toLong() }.toLongArray()
            is Number -> longArrayOf(data.toLong())
            null -> null
            else -> throw IllegalArgumentException(
                "Unsupported data type for input '${inputDesc.name}': ${data::class.simpleName}"
            )
        }

        // 2. 构建 OnnxRtShape，仅当 longArray 不为 null 时才调用 .data()
        val inputShape = inputDesc.type?.let { type ->
            val builder = OnnxRtShape(arena)
                .shape(inputDesc.name, inputDesc.shape)
                .type(type.i())
//            println("data is empty")
            // 关键：只有非空数据才设置 data
            if (longArray != null) builder.data(longArray) else builder
        }

        // 3. 设置输入（无论是否有 data，都执行 SetInput）
        val res = inputShape?.let { shape ->
            onnxLLmModel.onnxRtFramework().onnxRtLlmInf.SetInput(
                onnxLLmModel.onnxRtFramework().framework(),
                shape.memorySegment,
                onnxRtInput.memorySegment,
            )
        }

        if (res != 0) {
            val shapeStr = inputDesc.shape.joinToString(" ")
            throw OrtLLmShapeIsUnKnow("input shape is unknown ${inputDesc.name}:[$shapeStr], res=$res")
        }
    }
}