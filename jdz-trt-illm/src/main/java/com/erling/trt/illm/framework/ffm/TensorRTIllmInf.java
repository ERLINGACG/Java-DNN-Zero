package com.erling.trt.illm.framework.ffm;

import java.lang.foreign.MemorySegment;

public interface TensorRTIllmInf {
    MemorySegment CreateTensorRTIllmFramework(MemorySegment config_path);

    void DestroyTensorRTIllmFramework(MemorySegment framework);

    int CreateTensorRTIllmEngine(MemorySegment framework);

    int  CreateTensorRTIllmCtx(MemorySegment framework, MemorySegment ctx);

    int SetSampler(MemorySegment ctx, MemorySegment sampler);

    int RegisterKvMapping(MemorySegment framework, MemorySegment past_kv_str, MemorySegment present_kv_str);

    int GetTensorRTIllmEngineInfo(MemorySegment framework);


    int RegisterInput(MemorySegment framework, MemorySegment name, MemorySegment type);

    int AutoInputIds(MemorySegment ctx, MemorySegment input_ids_arr, long input_ids_len);

    int AutoPastKvCacheDefault(MemorySegment ctx, MemorySegment past_dims, long past_dims_len);

    int AutoPresentKvCacheDefault(MemorySegment ctx);

    int AutoLogits(MemorySegment ctx);

    int PrefillDefault(MemorySegment ctx);

    int DecodeDefault(MemorySegment ctx);

}
