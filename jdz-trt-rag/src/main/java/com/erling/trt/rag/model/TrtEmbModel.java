package com.erling.trt.rag.model;

import com.erling.trt.rag.framework.ffm.TrtEmbFrameFFM;
import com.erling.trt.rag.model.data.FillMode;

import com.erling.trt.rag.model.data.InputDims;
import com.erling.trt.rag.model.data.OutPutDims;
import com.erling.trt.rag.struct.TrtEmbCtx;
import com.erling.trt.rag.struct.TrtEmbData;

import java.lang.foreign.Arena;
import java.util.function.Supplier;

public class TrtEmbModel {

    TrtEmbFrameFFM embFrameFFM;

    public TrtEmbModel setFrameFFM(TrtEmbFrameFFM frameFFM) {
        this.embFrameFFM = frameFFM;
        return this;
    }

   public TrtEmbModel creatCtx(TrtEmbCtx ctx) {
        return RunKt.creatCtx(this,ctx);
    }

    public TrtEmbModel setDims(Arena arena, Supplier<InputDims> dimsRecordSupplier) {
        return RunKt.setDims(this,arena,dimsRecordSupplier);
    }

    public TrtEmbModel getDims(Arena arena, Supplier<OutPutDims> dimsRecordSupplier) {
        return RunKt.getDims(this,arena,dimsRecordSupplier);
    }

    public TrtEmbModel postCudaInt32(TrtEmbCtx ctx, Arena arena, String inputName,int[] inputValues,FillMode fillMode) {
        return RunKt.postCudaInt32(this,ctx,arena,inputName,inputValues,fillMode);
    }
    public TrtEmbModel postCudaFloat32(TrtEmbCtx ctx, Arena arena, float[] inputValues,String inputName,FillMode fillMode) {
        return RunKt.postCudaFloat32(this,ctx,arena,inputName,inputValues,fillMode);
    }

    public TrtEmbModel postCudaInt64(TrtEmbCtx ctx, Arena arena, String inputName,long[] inputValues,FillMode fillMode) {
        return RunKt.postCudaInt64(this,ctx,arena,inputName,inputValues,fillMode);
    }

    public TrtEmbModel forward(TrtEmbCtx ctx) {
        return RunKt.forward(this,ctx);
    }

    public TrtEmbModel forwardAsync(TrtEmbCtx ctx) {
        return RunKt.ForwardAsync(this,ctx);
    }

    public TrtEmbModel synchronize(TrtEmbCtx ctx) {
        return RunKt.synchronize(this,ctx);
    }

    public TrtEmbModel getEmbedding(TrtEmbCtx ctx, Arena arena, TrtEmbData embedding, String name) {
        return RunKt.getEmbedding(this,ctx,arena,embedding,name);
    }

    public TrtEmbModel getPooledEmbedding(TrtEmbCtx ctx,
                                          Arena arena,
                                          TrtEmbData embedding,
                                          String hidden_state_name,
                                          String attention_mask_name
    ) {
        return RunKt.getPooledEmbedding(this,ctx,arena,embedding,hidden_state_name,attention_mask_name);
    }

    public TrtEmbModel clearBindings(TrtEmbCtx ctx) {
        return RunKt.clearBindings(this,ctx);
    }
}
