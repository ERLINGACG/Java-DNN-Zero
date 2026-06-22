package com.erling.trt.illm.rt.model

import com.erling.trt.illm.struct.TensorRTIllmCtx
import com.erling.trt.illm.struct.TensorRTIllmSampleParams
import java.lang.foreign.Arena
import java.lang.foreign.ValueLayout.JAVA_LONG

fun createLlmCtx(tensorRTBaseLlmModel: TensorRTBaseLlmModel, ctx: TensorRTIllmCtx): TensorRTBaseLlmModel {

    tensorRTBaseLlmModel.tensorRTIllmFramework().tensorRTIllmInf.CreateTensorRTIllmCtx(
        tensorRTBaseLlmModel.tensorRTIllmFramework().framework,
        ctx.memorySegment
    )
    return tensorRTBaseLlmModel
}
fun setSampler(
    tensorRTBaseLlmModel: TensorRTBaseLlmModel,
    ctx: TensorRTIllmCtx,
    sampler: TensorRTIllmSampleParams,
): TensorRTBaseLlmModel {
    val code=tensorRTBaseLlmModel.tensorRTIllmFramework().tensorRTIllmInf.SetSampler(
        ctx.memorySegment,
        sampler.memorySegment
    )
    if(code!=0){
        throw RuntimeException("SetSampler failed:$code")
    }
    return tensorRTBaseLlmModel
}


fun registerKvMapping(
    tensorRTBaseLlmModel: TensorRTBaseLlmModel,
    kvMappingMap: MutableMap<String, String>
): TensorRTBaseLlmModel {

    kvMappingMap.forEach { (string, string1) ->
        val code=tensorRTBaseLlmModel.tensorRTIllmFramework().tensorRTIllmInf.RegisterKvMapping(
            tensorRTBaseLlmModel.tensorRTIllmFramework().framework,
            tensorRTBaseLlmModel.arena().allocateUtf8String(string),
            tensorRTBaseLlmModel.arena().allocateUtf8String(string1)
        )
        if(code!=0){
            throw RuntimeException("RegisterKvMapping failed:$code")
        }
    }

    return tensorRTBaseLlmModel
}

fun getTensorEngineInfo(tensorRTBaseLlmModel: TensorRTBaseLlmModel): TensorRTBaseLlmModel {

    val code=tensorRTBaseLlmModel.tensorRTIllmFramework().tensorRTIllmInf.GetTensorRTIllmEngineInfo(
        tensorRTBaseLlmModel.tensorRTIllmFramework().framework
    )
    if(code!=0){
        throw RuntimeException("GetTensorEngineInfo failed:$code")
    }
    return tensorRTBaseLlmModel
}

fun registerInput(
    tensorRTBaseLlmModel: TensorRTBaseLlmModel,
    name: String,
    type: String
): TensorRTBaseLlmModel {
    val code=tensorRTBaseLlmModel.tensorRTIllmFramework().tensorRTIllmInf.RegisterInput(
        tensorRTBaseLlmModel.tensorRTIllmFramework().framework,
        tensorRTBaseLlmModel.arena().allocateUtf8String(name),
        tensorRTBaseLlmModel.arena().allocateUtf8String(type)
    )
    if(code!=0){
        throw RuntimeException("RegisterInput failed:$code")
    }
    return tensorRTBaseLlmModel
}

fun autoInputIds(
    tensorRTBaseLlmModel: TensorRTBaseLlmModel,
    ctx: TensorRTIllmCtx,
    arena: Arena,
    inputIds: LongArray
): TensorRTBaseLlmModel {
    val code=tensorRTBaseLlmModel.tensorRTIllmFramework().tensorRTIllmInf.AutoInputIds(
        ctx.memorySegment,
        arena.allocateArray(JAVA_LONG, *inputIds),
        inputIds.size.toLong()
    )
    if(code!=0){
        throw RuntimeException("AutoInputIds failed:$code")
    }
    return tensorRTBaseLlmModel
}
fun autoPastKvCacheDefault(
    tensorRTBaseLlmModel: TensorRTBaseLlmModel,
    ctx: TensorRTIllmCtx,
    arena: Arena,
    pastDims: LongArray
): TensorRTBaseLlmModel {
    val code=tensorRTBaseLlmModel.tensorRTIllmFramework().tensorRTIllmInf.AutoPastKvCacheDefault(
        ctx.memorySegment,
        arena.allocateArray(JAVA_LONG, *pastDims),
        pastDims.size.toLong()
    )
    if(code!=0){
        throw RuntimeException("AutoPastKvCacheDefault failed:$code")
    }
    return tensorRTBaseLlmModel
}

fun autoPresentKvCacheDefault(
    tensorRTBaseLlmModel: TensorRTBaseLlmModel,
    ctx: TensorRTIllmCtx
): TensorRTBaseLlmModel {
    val code=tensorRTBaseLlmModel.tensorRTIllmFramework().tensorRTIllmInf.AutoPresentKvCacheDefault(
        ctx.memorySegment
    )
    if(code!=0){
        throw RuntimeException("AutoPresentKvCacheDefault failed:$code")
    }
    return tensorRTBaseLlmModel
}
fun autoLogits(
    tensorRTBaseLlmModel: TensorRTBaseLlmModel,
    ctx: TensorRTIllmCtx
): TensorRTBaseLlmModel {
    val code=tensorRTBaseLlmModel.tensorRTIllmFramework().tensorRTIllmInf.AutoLogits(
        ctx.memorySegment
    )
    if(code!=0){
        throw RuntimeException("AutoLogits failed:$code")
    }
    return tensorRTBaseLlmModel
}
fun prefillDefault(
    tensorRTBaseLlmModel: TensorRTBaseLlmModel,
    ctx: TensorRTIllmCtx
): TensorRTBaseLlmModel {
    val code=tensorRTBaseLlmModel.tensorRTIllmFramework().tensorRTIllmInf.PrefillDefault(
        ctx.memorySegment
    )
    if(code!=0){
        throw RuntimeException("PrefillDefault failed:$code")
    }
    return tensorRTBaseLlmModel
}
fun decodeDefault(
    tensorRTBaseLlmModel: TensorRTBaseLlmModel,
    ctx: TensorRTIllmCtx
): TensorRTBaseLlmModel {
    val code=tensorRTBaseLlmModel.tensorRTIllmFramework().tensorRTIllmInf.DecodeDefault(
        ctx.memorySegment
    )
    if(code!=0){
        throw RuntimeException("DecodeDefault failed:$code")
    }
    return tensorRTBaseLlmModel
}

