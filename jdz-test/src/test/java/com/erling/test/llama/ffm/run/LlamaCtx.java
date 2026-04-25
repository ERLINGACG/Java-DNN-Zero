package com.erling.test.llama.ffm.run;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

public class LlamaCtx extends NativeStruct<LlamaCtx> {

    public C_POINTER  context;
    public C_POINTER  sampler;
    public LlamaCtx(Arena arena) {
        super(LlamaCtx.class, arena);
//        initFields(this);
    }
}
