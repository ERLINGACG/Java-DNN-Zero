package com.erling.trt.rag.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

public class TrtEmbCtx extends NativeStruct<TrtEmbCtx> {

    public C_POINTER context;

    public C_POINTER bindings;
    public TrtEmbCtx(Arena arena) {
        super(TrtEmbCtx.class, arena);
    }
}
