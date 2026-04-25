package com.erling.llama.llm.framework.jna;

import com.erling.core.load.ann.DyLinkLibInf;
import com.erling.core.load.ann.InitType;
import com.erling.llama.llm.backend.LlamaCallBack;
import com.erling.llama.llm.backend.LlamaInvokeBackEnd;
import com.erling.llama.llm.framework.jna.struct.LLM_GGUF_Context;
import com.erling.llama.llm.framework.jna.struct.LLM_GGUF_Context_RTParam;
import com.sun.jna.Pointer;
import lombok.Getter;

import java.util.function.Supplier;

public class LlamaCppFrameWorkJNA implements LlamaInvokeBackEnd<LLM_GGUF_Context_RTParam,LLM_GGUF_Context> {

    @Getter
    private Pointer framework;

    @DyLinkLibInf
    @Getter
    private LlamaCppInf frameWorkInf;

    private final String conf_path;

    public LlamaCppFrameWorkJNA(String conf_path){
        this.conf_path = conf_path;
    }



    @InitType
    public void init_model(){
        framework = frameWorkInf.CreateLLm_GGuf_Framework(conf_path);
    }
    public void destroy_model(){
        frameWorkInf.DestroyLLm_GGuf_Framework(framework);
    }

    @Override
    public float[] embeddings(
            String prompt,
            boolean clear,
            Supplier<LLM_GGUF_Context_RTParam> ctx, Supplier<LLM_GGUF_Context> params
    ) {
        return new float[0];
    }


    public void recording(String prompt,
            Supplier<LLM_GGUF_Context_RTParam> rtSupplier,
            Supplier<LLM_GGUF_Context> ctxSupplier,
            LlamaCallBack callBack
    ){
         LlamaCppRecodingKt.recording(
                 this,
                 prompt,
                 -1,
                 rtSupplier::get,
                 ctxSupplier::get,
                 callBack
         );
    }

   @Override
    public void recording(
           String prompt,
           int eos,
           boolean clear, Supplier<LLM_GGUF_Context_RTParam> ctx,
           Supplier<LLM_GGUF_Context> params,
           LlamaCallBack callBack
    ){
        LlamaCppRecodingKt.recording(
                this,
                prompt,
                eos,
                ctx::get,
                params::get,
                callBack
        );
    }

}
