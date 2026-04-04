package com.erling.llama.llm.framework;

import com.erling.core.load.ann.DyLinkLibInf;
import com.erling.core.load.ann.InitType;
import com.erling.llama.llm.struct.LLM_GGUF_Context;
import com.erling.llama.llm.struct.LLM_GGUF_Context_RTParam;
import com.sun.jna.Pointer;
import lombok.Getter;

import java.util.function.Supplier;

public class LlamaCppFrameWork {

    @Getter
    private Pointer framework;

    @DyLinkLibInf
    @Getter
    private LlamaCppInf frameWorkInf;

    private final String conf_path;

    public LlamaCppFrameWork(String conf_path){
        this.conf_path = conf_path;
    }

    @InitType
    public void init_model(){
        framework = frameWorkInf.CreateLLm_GGuf_Framework(conf_path);
    }
    public void destroy_model(){
        frameWorkInf.DestroyLLm_GGuf_Framework(framework);
    }

    public void recording(String prompt,
            Supplier<LLM_GGUF_Context_RTParam> rtSupplier,
            Supplier<LLM_GGUF_Context> ctxSupplier,
            RecCallback recCallback
    ){
         LlamaCppRecodingKt.recording(
                 this,
                 prompt,
                 -1,
                 rtSupplier::get,
                 ctxSupplier::get,
                 recCallback
         );
    }

    public void recording(String prompt,
                          int eos,
                          Supplier<LLM_GGUF_Context_RTParam> rtSupplier,
                          Supplier<LLM_GGUF_Context> ctxSupplier,
                          RecCallback recCallback
    ){
        LlamaCppRecodingKt.recording(
                this,
                prompt,
                eos,
                rtSupplier::get,
                ctxSupplier::get,
                recCallback
        );
    }

}
