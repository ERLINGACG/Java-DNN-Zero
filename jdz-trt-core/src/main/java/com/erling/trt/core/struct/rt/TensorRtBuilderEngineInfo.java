package com.erling.trt.core.struct.rt;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

public class TensorRtBuilderEngineInfo extends NativeStruct<TensorRtBuilderEngineInfo> {

    public C_POINTER shapes;
    public TensorRtBuilderEngineInfo(Arena arena) {
        super(TensorRtBuilderEngineInfo.class, arena);
    }
}
