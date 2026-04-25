package com.erling.llama.llm.framework.jna.struct;

import com.sun.jna.Structure;

//struct  LLM_GGUF_Stream {
//char stream[1024]{};
//        };
@Structure.FieldOrder({"stream","str_len"})
public class LLM_GGUF_Stream extends Structure {
    public byte[] stream = new byte[1024];
    public int  str_len= 0;

    public String getStream(){
        return new String(stream,0,str_len);
    }
}
