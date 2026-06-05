package com.erling.ort.emd.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

public class OnnxRtEmdCtx extends NativeStruct<OnnxRtEmdCtx> {
    public C_POINTER shape;

    public OnnxRtEmdCtx(Arena arena) {
        super(OnnxRtEmdCtx.class, arena);
    }
}
