package com.erling.ort.llm.model.rt

import com.erling.ort.llm.model.OnnxLLmModel
import com.erling.ort.llm.struct.OnnxRtLlmCtx
import com.erling.ort.llm.struct.OnnxRtLlmKv
import com.erling.ort.llm.struct.OnnxRtLlmParam
import java.lang.foreign.Arena
import java.lang.foreign.ValueLayout.JAVA_LONG



fun setSample(model: OnnxLLmModel,param: OnnxRtLlmParam,ctx: OnnxRtLlmCtx
){
    val ret=model.onnxRtLlmFramework().onnxRtLlmInf.InitSampler(
        param.memorySegment,
        ctx.memorySegment,
    )
    if(ret!=0){
        throw RuntimeException("setSampler failed")
    }
}

fun resetCtx(model: OnnxLLmModel, ctx: OnnxRtLlmCtx){
    val ret=model.onnxRtLlmFramework().onnxRtLlmInf.ResetCtx(
        ctx.memorySegment,
    )
    if(ret!=0){
        throw RuntimeException("resetCtx failed")
    }
}
fun resetKv(model: OnnxLLmModel, kv: OnnxRtLlmKv){
    val ret=model.onnxRtLlmFramework().onnxRtLlmInf.ResetKv(
        kv.memorySegment,
    )
    if(ret!=0){
        throw RuntimeException("resetKv failed")
    }
}

fun prefillFor1DRoPE(model: OnnxLLmModel,arena: Arena, ctx: OnnxRtLlmCtx,kv: OnnxRtLlmKv,ids: LongArray) {
    model.onnxRtLlmFramework().onnxRtLlmInf.PrefillFor1DRoPE(
        model.onnxRtLlmFramework().framework(),
        ctx.memorySegment,
        kv.memorySegment,
        arena.allocateArray(JAVA_LONG,*ids),
        ids.size.toLong()
    )
}

fun decodeFor1DRoPE(model: OnnxLLmModel, ctx: OnnxRtLlmCtx, kv: OnnxRtLlmKv){
    model.onnxRtLlmFramework().onnxRtLlmInf.DecodeFor1DRoPE(
        model.onnxRtLlmFramework().framework(),
        ctx.memorySegment,
        kv.memorySegment,
    )
}

