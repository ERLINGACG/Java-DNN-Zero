package com.erling.test.llama.ffm.run;

import com.erling.core.load.ffm.api.cpp.clasz.NativeClass;
import com.erling.core.load.ffm.api.cpp.hook.AutoInit;
import com.erling.core.load.ffm.api.cpp.hook.CreatProxy;
import com.erling.core.load.ffm.api.cpp.hook.InitFunction;
import com.erling.llama.llm.backend.LlamaCallBack;
import com.erling.llama.llm.backend.LlamaInvokeBackEnd;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;
import java.util.Map;
import java.util.function.Supplier;

public class LlamaToCppFFm implements NativeClass , LlamaInvokeBackEnd<LlamaCtx,LlamaParams> {

    Map<String,MethodHandle> functionMap;

    MemorySegment modelSegment;

    String modelPath;


    @CreatProxy(mappingClass = LlamaCppInf.class)
    public LlamaCppInf cppInf;

    @Override
    @InitFunction
    public void setFunction(Map<String, MethodHandle> functionMap) {
        this.functionMap=functionMap;
    }

     @AutoInit
     public void AutoInit(String modelPath){
        this.modelPath = modelPath;
        if(cppInf != null){
            init_model();
        }
     }

    @Override
    public MethodHandle getFunction(String name) {
        return this.functionMap.get(name);
    }

    @Override
    public Map<String, MethodHandle> getFunctionMap() {
        return this.functionMap;
    }


    @Override
    public void recording(
            String prompt, int eos,
            boolean clear, Supplier<LlamaCtx> ctx,
            Supplier<LlamaParams> params,
            LlamaCallBack callBack
    ) {
        var llamaCtx= ctx.get();
        var llamaParams = params.get();

        try (Arena arena = Arena.ofConfined()){
            LlamaBatch batch = new LlamaBatch(arena);
            int tokenizer_len = cppInf.InitBatchASync(modelSegment,
                    arena.allocateUtf8String(prompt),
                    batch.getMemorySegment(),
                    llamaCtx.getMemorySegment()
            );
            cppInf.SetSamplerASync(modelSegment,
                    llamaParams.getMemorySegment(),
                    llamaCtx.getMemorySegment()
            );
            int set_eos;
            if(eos <= 0){
                set_eos = batch.eos.get();
            }else{
                set_eos = eos;
            }
            int token_count = 0;
            do{
                LlamaStream stream = new LlamaStream(arena);
                cppInf.ReasoningASync(modelSegment, //模型指针
                        batch.getMemorySegment(),   //轮次指针
                        llamaCtx.getMemorySegment(), //上下文指针
                        stream.getMemorySegment() //输出指针
                );
                token_count++;
                if(!callBack.invoke(stream.stream.getString(),token_count,tokenizer_len)){
                    break;
                }
            }while(batch.next_token.get() != set_eos);

        }

    }

    @Override
    public void init_model() {
        try (Arena arena = Arena.ofConfined()) {
            this.modelSegment =
                    cppInf.CreateLLm_GGuf_Framework(arena.allocateUtf8String(modelPath));
        }
    }

    @Override
    public void destroy_model() {

    }

    @Override
    public float[] embeddings(String prompt, boolean clear,
                            Supplier<LlamaCtx> ctx, Supplier<LlamaParams> params) {
        return new float[0];
    }
}
