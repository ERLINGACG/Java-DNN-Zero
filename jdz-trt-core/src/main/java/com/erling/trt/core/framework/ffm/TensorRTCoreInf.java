package com.erling.trt.core.framework.ffm;

import java.lang.foreign.MemorySegment;

public interface TensorRTCoreInf {
//    void TestLoad(MemorySegment path);
//    void TestLoad2(MemorySegment path);

    int InitBuilderEngineInfo(MemorySegment builderEngineInfo);

    int RegisterShapeInfo(MemorySegment shapeInfo, MemorySegment builderEngineInfo);

    int GetShapeInfoDetail(MemorySegment builderEngineInfo);

    int InitBuilderEngine(MemorySegment memorySegment);

    int BuildEngine(MemorySegment engine,MemorySegment config,MemorySegment builderEngineInfo);
}
