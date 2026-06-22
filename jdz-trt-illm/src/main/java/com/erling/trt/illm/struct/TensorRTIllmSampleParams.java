package com.erling.trt.illm.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_FLOAT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT64;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

public class TensorRTIllmSampleParams extends NativeStruct<TensorRTIllmSampleParams> {
    public C_INT64 top_k;

    public C_FLOAT top_p;

    public C_FLOAT temp;
    public TensorRTIllmSampleParams(Arena arena) {
        super(TensorRTIllmSampleParams.class, arena);
        initFields(this);
    }
}
