package com.erling.trt.rag.model

import com.erling.trt.rag.exc.TrtException
import com.erling.trt.rag.model.data.FillMode
import com.erling.trt.rag.model.data.InputDims
import com.erling.trt.rag.model.data.OutPutDims
import com.erling.trt.rag.struct.TrtEmbCtx
import com.erling.trt.rag.struct.TrtEmbData
import java.lang.foreign.Arena
import java.lang.foreign.ValueLayout.JAVA_FLOAT
import java.lang.foreign.ValueLayout.JAVA_INT
import java.lang.foreign.ValueLayout.JAVA_LONG
import java.util.function.Supplier

fun creatCtx(model: TrtEmbModel, ctx: TrtEmbCtx): TrtEmbModel {
    val ret =  model.embFrameFFM.frameInf.CreateCtx(
        model.embFrameFFM.modelSegment(),
        ctx.memorySegment
    )
    if(ret != 0){
        throw TrtException("CreateCtx failed")
    }
    return model
}

fun setDims(model: TrtEmbModel, arena: Arena, inputDimsSupplier: Supplier<InputDims>): TrtEmbModel {
    val dimsRecord = inputDimsSupplier.get()
    val ret = model.embFrameFFM.frameInf.SetDims(
        dimsRecord.ctx().memorySegment,
        arena.allocateUtf8String(dimsRecord.dims_name()),
        arena.allocateArray(JAVA_INT,*dimsRecord.dims()),
        dimsRecord.dims().size
    )
    if(ret != 0){
        throw TrtException("SetDims failed for dims ${dimsRecord.dims_name()}")
    }
    return model
}

fun getDims(model: TrtEmbModel, arena: Arena, outPutDimsSupplier: Supplier<OutPutDims>): TrtEmbModel {
    val dimsRecord = outPutDimsSupplier.get()
    val ret = model.embFrameFFM.frameInf.GetDims(
        model.embFrameFFM.modelSegment(),
        dimsRecord.ctx().memorySegment,
        arena.allocateUtf8String(dimsRecord.output_name())
    )
    if(ret != 0){
        throw TrtException("GetDims failed for output ${dimsRecord.output_name()}")
    }
    return model
}

fun postCudaInt32(model: TrtEmbModel,
                  ctx: TrtEmbCtx,
                  arena: Arena,
                  inputName: String,
                  inputValues: IntArray,fillMode: FillMode): TrtEmbModel {
    val fillType: Int = when(fillMode){
        FillMode.NORMAL -> 0
        FillMode.ALL_ZERO -> 1
        FillMode.ALL_ONE -> 2
    }

    val ret = model.embFrameFFM.frameInf.PostCuda(
        model.embFrameFFM.modelSegment(),
        ctx.memorySegment,
        arena.allocateUtf8String(inputName),
        arena.allocateArray(JAVA_INT,*inputValues),
        inputValues.size,
        fillType
    )
    if(ret != 0){
        throw TrtException("PostCudaInt32 failed for $inputName")
    }
    return model
}

fun postCudaFloat32(model: TrtEmbModel,
                  ctx: TrtEmbCtx,
                  arena: Arena,
                  inputName: String,
                  inputValues: FloatArray,fillMode: FillMode): TrtEmbModel {
    val fillType: Int = when(fillMode){
        FillMode.NORMAL -> 0
        FillMode.ALL_ZERO -> 1
        FillMode.ALL_ONE -> 2
    }

    val ret = model.embFrameFFM.frameInf.PostCuda(
        model.embFrameFFM.modelSegment(),
        ctx.memorySegment,
        arena.allocateUtf8String(inputName),
        arena.allocateArray(JAVA_FLOAT,*inputValues),
        inputValues.size,
        fillType
    )
    if(ret != 0){
        throw TrtException("PostCudaFloat32 failed for $inputName")
    }
    return model
}
fun postCudaInt64(model: TrtEmbModel,
                    ctx: TrtEmbCtx,
                    arena: Arena,
                    inputName: String,
                    inputValues: LongArray,fillMode: FillMode): TrtEmbModel {
    val fillType: Int = when(fillMode){
        FillMode.NORMAL -> 0
        FillMode.ALL_ZERO -> 1
        FillMode.ALL_ONE -> 2
    }

    val ret = model.embFrameFFM.frameInf.PostCuda(
        model.embFrameFFM.modelSegment(),
        ctx.memorySegment,
        arena.allocateUtf8String(inputName),
        arena.allocateArray(JAVA_LONG,*inputValues),
        inputValues.size,
        fillType
    )
    if(ret != 0){
        throw TrtException("PostCudaInt64 failed for $inputName")
    }
    return model
}

fun forward(model: TrtEmbModel, ctx: TrtEmbCtx): TrtEmbModel {
    val ret = model.embFrameFFM.frameInf.Forward(
        model.embFrameFFM.modelSegment(),
        ctx.memorySegment
    )
    if(ret != 0){
        throw TrtException("Forward failed")
    }
    return model
}

fun ForwardAsync(model: TrtEmbModel, ctx: TrtEmbCtx): TrtEmbModel {
    val ret = model.embFrameFFM.frameInf.ForwardAsync(
        model.embFrameFFM.modelSegment(),
        ctx.memorySegment
    )
    if(ret != 0){
        throw TrtException("ForwardAsync failed")
    }
    return model
}

fun synchronize(model: TrtEmbModel, ctx: TrtEmbCtx): TrtEmbModel {
    val ret = model.embFrameFFM.frameInf.Synchronize(
        ctx.memorySegment
    )
    if(ret != 0){
        throw TrtException("Synchronize failed")
    }
    return model
}

fun getEmbedding(model: TrtEmbModel, ctx: TrtEmbCtx, arena: Arena,embedding: TrtEmbData, name: String): TrtEmbModel {
    val ret = model.embFrameFFM.frameInf.GetEmbedding(
        model.embFrameFFM.modelSegment(),
        ctx.memorySegment,
        embedding.memorySegment,
        arena.allocateUtf8String(name)
    )
    if(ret != 0){
        throw TrtException("GetEmbedding failed for $name")
    }
    return model
}

fun getPooledEmbedding(model: TrtEmbModel,
                       ctx: TrtEmbCtx,
                       arena: Arena,
                       embedding: TrtEmbData,
                       hidden_state_name: String,
                       attention_mask_name: String
): TrtEmbModel {
    val ret = model.embFrameFFM.frameInf.GetPooledEmbedding(
        model.embFrameFFM.modelSegment(),
        ctx.memorySegment,
        embedding.memorySegment,
        arena.allocateUtf8String(hidden_state_name),
        arena.allocateUtf8String(attention_mask_name)
    )
    if(ret != 0){
        throw TrtException("GetPooledEmbedding failed for $hidden_state_name and $attention_mask_name")
    }
    return model
}

fun clearBindings(model: TrtEmbModel,ctx: TrtEmbCtx): TrtEmbModel {
    model.embFrameFFM.frameInf.ClearBindings(
        ctx.memorySegment,
    )
    return model
}

