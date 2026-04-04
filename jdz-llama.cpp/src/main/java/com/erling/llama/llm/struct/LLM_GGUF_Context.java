package com.erling.llama.llm.struct;


import com.sun.jna.Pointer;
import com.sun.jna.Structure;

@Structure.FieldOrder({"context", "sampler"})
public class LLM_GGUF_Context extends Structure {
    public Pointer context;
    public Pointer sampler;
}


