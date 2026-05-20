package com.erling.ort.core.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

/**
 * struct OnnxRTEngine {
 *         std::unique_ptr<Ort::SessionOptions> sessionOptions;
 *         std::unique_ptr<Ort::Session> session;
 *         std::unique_ptr<OrtCUDAProviderOptions> cudaProviderOptions;
 *     };
 */
public class OnnxRTEngine extends NativeStruct<OnnxRTEngine> {

    public C_POINTER sessionOptions;
    public C_POINTER session;
    public C_POINTER cudaProviderOptions;

    public C_POINTER input_names_ptr;

    public C_POINTER output_names_ptr;



    public OnnxRTEngine(Arena arena) {
        super(OnnxRTEngine.class, arena);
        initFields(this);
    }
}
