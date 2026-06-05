package com.erling.ort.llm.framework.ffm;

import java.lang.foreign.MemorySegment;

public interface OnnxRtLlmInf {

    void GetCtxInfo(MemorySegment ctx);

    MemorySegment CreateLLmFramework(MemorySegment framework);

    int GetHeads(MemorySegment framework);

    int GetLayers(MemorySegment framework);

    int  GetHeadDim(MemorySegment framework);

    int ResetCtx(MemorySegment ctx);

    int ResetKv(MemorySegment kv);

    int PrefillFor1DRoPE(MemorySegment framework, MemorySegment ctx,
                         MemorySegment kv,        MemorySegment tokens,long len);

    int DecodeFor1DRoPE(MemorySegment framework, MemorySegment ctx,
                        MemorySegment kv);

    int InitSampler(MemorySegment param, MemorySegment ctx);

    int  InitBatchForTokenIds(MemorySegment framework, MemorySegment ctx,
                              MemorySegment kv,        MemorySegment tokens,long len);
    int  GenerateToken(MemorySegment framework, MemorySegment ctx,
                        MemorySegment kv);
}
