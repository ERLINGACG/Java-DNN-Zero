package com.erling.trt.illm.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT64;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

public class TensorRTIllmCtx extends NativeStruct<TensorRTIllmCtx>  {

    public C_POINTER core_ctx;
    public C_POINTER sample;
    public C_POINTER input_inputs;
    public C_POINTER kv_mapping;
//    public C_INT64 seq_len;
    public C_INT64 next_token;
    public C_INT64 abs_token_pos;

    public TensorRTIllmCtx(Arena arena) {
        super(TensorRTIllmCtx.class, arena);
        initFields(this);
    }
}
