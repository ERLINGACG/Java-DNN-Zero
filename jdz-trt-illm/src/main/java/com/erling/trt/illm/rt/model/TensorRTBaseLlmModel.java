package com.erling.trt.illm.rt.model;

import com.erling.trt.illm.framework.ffm.TensorRTIllmFramework;
import com.erling.trt.illm.struct.TensorRTIllmCtx;
import com.erling.trt.illm.struct.TensorRTIllmSampleParams;

import java.lang.foreign.Arena;
import java.util.Map;
import java.util.function.Supplier;

public class TensorRTBaseLlmModel {

    private TensorRTIllmFramework tensorRTIllmFramework;

    private final Arena arena;

    public TensorRTBaseLlmModel(){
        this.arena = Arena.ofShared();
    }

    public Arena arena() {
        return arena;
    }

    public TensorRTBaseLlmModel setTensorRTIllmFramework(TensorRTIllmFramework tensorRTIllmFramework){
        this.tensorRTIllmFramework = tensorRTIllmFramework;
        return this;
    }

    public TensorRTBaseLlmModel registerKvMapping(Supplier<Map<String, String>> kvMapping){
        return TrtLlmOperationKt.registerKvMapping(this,kvMapping.get());
    }

    public TensorRTBaseLlmModel getTensorEngineInfo(){
        return TrtLlmOperationKt.getTensorEngineInfo(this);
    }

    public TensorRTIllmFramework tensorRTIllmFramework() {
        return tensorRTIllmFramework;
    }

    public TensorRTBaseLlmModel createLlmCtx(TensorRTIllmCtx ctx){
        return TrtLlmOperationKt.createLlmCtx(this,ctx);
    }

    public TensorRTBaseLlmModel setSampler(TensorRTIllmCtx ctx, TensorRTIllmSampleParams sampleParams) {
        return TrtLlmOperationKt.setSampler(this,ctx,sampleParams);
    }


    public TensorRTBaseLlmModel registerInput(String name, String type){
        return TrtLlmOperationKt.registerInput(this,name,type);
    }

    public TensorRTBaseLlmModel autoInputIds(TensorRTIllmCtx ctx, Arena arena, long[] inputIds){
        return TrtLlmOperationKt.autoInputIds(this,ctx,arena,inputIds);
    }
    public TensorRTBaseLlmModel autoPastKvCacheDefault(TensorRTIllmCtx ctx, Arena arena, long[] pastDims){
        return TrtLlmOperationKt.autoPastKvCacheDefault(this,ctx,arena,pastDims);
    }
    public TensorRTBaseLlmModel autoPresentKvCacheDefault(TensorRTIllmCtx ctx){
        return TrtLlmOperationKt.autoPresentKvCacheDefault(this,ctx);
    }
    public TensorRTBaseLlmModel autoLogits(TensorRTIllmCtx ctx){
        return TrtLlmOperationKt.autoLogits(this,ctx);
    }
    public TensorRTBaseLlmModel prefillDefault(TensorRTIllmCtx ctx){
        return TrtLlmOperationKt.prefillDefault(this,ctx);
    }
    public TensorRTBaseLlmModel decodeDefault(TensorRTIllmCtx ctx){
        return TrtLlmOperationKt.decodeDefault(this,ctx);
    }
}
