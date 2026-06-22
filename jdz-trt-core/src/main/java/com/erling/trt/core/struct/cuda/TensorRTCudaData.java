package com.erling.trt.core.struct.cuda;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT64;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

public class TensorRTCudaData extends NativeStruct<TensorRTCudaData> {

    public C_POINTER data;

    public C_INT64 len;
    public TensorRTCudaData(Arena arena) {
        super(TensorRTCudaData.class, arena);
        initFields(this);
    }
}
