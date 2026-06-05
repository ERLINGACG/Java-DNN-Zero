package com.erling.ort.llm.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

public class OnnxRtLlmKv extends NativeStruct<OnnxRtLlmKv> {

    public C_POINTER prefixKvBuffer;
    public C_POINTER prefixKv;

    public C_POINTER  KvBuffer;
    public C_POINTER kv;

    public OnnxRtLlmKv(Arena arena) {
        super(OnnxRtLlmKv.class, arena);
    }
}
