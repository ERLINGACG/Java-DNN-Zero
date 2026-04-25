package com.erling.llama.llm.framework.ffm.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

//struct LLM_GGUF_Batch {
//std::unique_ptr<llama_batch> batch{};
//int next_token = LLAMA_TOKEN_NULL;
//int eos{};
//
//        };
public class LlamaBatch extends NativeStruct<LlamaBatch> {

    public C_POINTER batch;
    public C_INT next_token;
    public C_INT eos;
    public LlamaBatch(Arena arena) {
        super(LlamaBatch.class, arena);
        initFields(this);
    }
}
