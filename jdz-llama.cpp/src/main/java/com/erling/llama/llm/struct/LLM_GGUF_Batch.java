package com.erling.llama.llm.struct;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

//struct LLM_GGUF_Batch {
//llama_batch* batch{};
//int32_t next_token = LLAMA_TOKEN_NULL;
//int32_t eos{};
//        };
@Structure.FieldOrder({"batch", "next_token", "eos"})
public class LLM_GGUF_Batch extends Structure {
    public Pointer batch;
    public int next_token;
    public int eos;
}
