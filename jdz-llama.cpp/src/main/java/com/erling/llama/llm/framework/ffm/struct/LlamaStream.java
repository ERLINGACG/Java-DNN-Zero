package com.erling.llama.llm.framework.ffm.struct;

import com.erling.core.load.ffm.api.cpp.struct.CArray;
import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_ARRAY;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

//struct  LLM_GGUF_Stream {
//char stream[1024] = {0};
//int  str_len= 0;
//    };
public class LlamaStream extends NativeStruct<LlamaStream> {

    @CArray(len = 1024)
    public C_ARRAY stream;

    public C_INT str_len;


    public LlamaStream(Arena arena) {
        super(LlamaStream.class, arena);
        initFields(this);
    }
}
