package com.erling.ort.llm.framework.ffm;

import java.lang.foreign.MemorySegment;

public interface OnnxRtLlmInf {
    void GetCtxSize();

    void GetEngineSize();

    void  GetKvSize(MemorySegment kv);

    void GetCtxInfo(MemorySegment ctx);

    void GetShapeInfo(MemorySegment shape);

    void ReleaseShapeInfo(MemorySegment shape);

    void GetLLmEngineInfo(MemorySegment framework);
    MemorySegment CreateLLmFramework(MemorySegment framework);

    int Prefill(MemorySegment framework, MemorySegment ctx, MemorySegment kv,MemorySegment input);

    int Decode(MemorySegment framework, MemorySegment ctx, MemorySegment kv,MemorySegment input);
    int SetInput(MemorySegment framework, MemorySegment shape, MemorySegment input);
    int InitSampler(MemorySegment param, MemorySegment ctx);

    int  InitBatchForTokenIds(MemorySegment framework, MemorySegment ctx,
                   MemorySegment kv,        MemorySegment tokens,long len);
    int  GenerateToken(MemorySegment framework, MemorySegment ctx,
                        MemorySegment kv);
}
