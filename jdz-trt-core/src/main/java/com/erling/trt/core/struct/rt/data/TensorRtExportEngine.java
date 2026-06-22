package com.erling.trt.core.struct.rt.data;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

public class TensorRtExportEngine extends NativeStruct<TensorRtExportEngine> {
    public C_POINTER engine;

    public TensorRtExportEngine(Arena arena) {
        super(TensorRtExportEngine.class, arena);
    }
}
