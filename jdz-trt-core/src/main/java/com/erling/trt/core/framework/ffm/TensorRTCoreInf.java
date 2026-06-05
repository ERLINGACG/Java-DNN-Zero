package com.erling.trt.core.framework.ffm;

import java.lang.foreign.MemorySegment;

public interface TensorRTCoreInf {
    void TestLoad(MemorySegment path);
}
