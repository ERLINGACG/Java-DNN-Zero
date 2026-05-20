package com.erling.ort.llm.struct;

import com.erling.core.load.ffm.api.cpp.hook.Padding;
import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

public class OnnxRtLlmEngine extends NativeStruct<OnnxRtLlmEngine> {

    public @Padding(8) C_POINTER core_engine;
    public C_INT head_dim;
    public C_INT heads;
    public C_INT layers;

    public OnnxRtLlmEngine(Arena arena) {
        super(OnnxRtLlmEngine.class, arena);
        initFields(this);
    }
}
