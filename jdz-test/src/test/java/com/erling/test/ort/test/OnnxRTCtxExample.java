package com.erling.test.ort.test;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;
//struct OnnxRTCtx {
//std::unique_ptr<Ort::SessionOptions> sessionOptions;
//std::unique_ptr<Ort::Session> session;
//std::unique_ptr<OrtCUDAProviderOptions> cudaProviderOptions;
//    };
public class OnnxRTCtxExample extends NativeStruct<OnnxRTCtxExample> {

    public C_POINTER sessionOptions;
    public C_POINTER session;
    public C_POINTER cudaProviderOptions;

    public OnnxRTCtxExample(Arena arena) {
        super(OnnxRTCtxExample.class, arena);
    }
}
