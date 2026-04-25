package com.erling.llama.llm.framework.ffm.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

//struct LLM_GGUF_Embedding
//    {
    //std::unique_ptr<float> embeddings;
    //int  embeddings_len= 0;
//    };
public class LlamaEmb extends NativeStruct<LlamaEmb> {
    public C_POINTER embeddings;
    public C_INT embeddings_len;
    public LlamaEmb(Arena arena) {
        super(LlamaEmb.class, arena);
        initFields(this);
    }
}
