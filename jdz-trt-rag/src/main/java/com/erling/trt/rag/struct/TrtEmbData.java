package com.erling.trt.rag.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

//struct  TrtEmbData {
//std::unique_ptr<float[]> embedding;
//int embedding_size;
//    };
public class TrtEmbData extends NativeStruct<TrtEmbData> {


    public C_POINTER embedding;
    public C_INT embedding_size;
    public TrtEmbData(Arena arena) {
        super(TrtEmbData.class, arena);
        initFields(this);
    }
}
