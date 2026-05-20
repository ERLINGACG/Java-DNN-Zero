package com.erling.test.ort.test;

import java.lang.foreign.MemorySegment;

public interface OnnxRTInf {

    MemorySegment CreateOnnxRtFramework(MemorySegment config_path);

    int CreateCtx(MemorySegment framework, MemorySegment ctx);

    void GetEngineInfo(MemorySegment framework, MemorySegment ctx);

    void Run(MemorySegment framework, MemorySegment ctx);

    int RunExam(MemorySegment framework,MemorySegment tokens, int len, MemorySegment ctx);
}
