package com.erling.llama.llm.framework.ffm.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

public class LlamaCtx extends NativeStruct<LlamaCtx> {

    public C_POINTER  context;
    public C_POINTER  sampler;
    public LlamaCtx(Arena arena) {
        super(LlamaCtx.class, arena);
    }
}
