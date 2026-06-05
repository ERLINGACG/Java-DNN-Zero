package com.erling.ort.emd.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

public class OnnxRtEmdData extends NativeStruct<OnnxRtEmdData>{

    public C_POINTER embedding;
    public C_INT     embedding_size;

    public OnnxRtEmdData(Arena arena) {
        super(OnnxRtEmdData.class, arena);
        initFields(this);
    }

    public float[] getEmbedding(){
        return embedding.getForFloatArray(embedding_size.get());
    }
}
