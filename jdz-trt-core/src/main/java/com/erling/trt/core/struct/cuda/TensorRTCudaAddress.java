package com.erling.trt.core.struct.cuda;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT64;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

public class TensorRTCudaAddress extends NativeStruct<TensorRTCudaAddress> {

    public C_POINTER address;

    public C_POINTER stream;

    public C_INT64 org_array_len;
    public TensorRTCudaAddress(Arena arena) {
        super(TensorRTCudaAddress.class, arena);
    }
}
