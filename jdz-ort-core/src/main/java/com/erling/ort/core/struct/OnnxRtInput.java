package com.erling.ort.core.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

public class OnnxRtInput extends NativeStruct<OnnxRtInput> {

    public C_POINTER input_ptr;
    public OnnxRtInput(Arena arena) {
        super(OnnxRtInput.class, arena);
    }
}
