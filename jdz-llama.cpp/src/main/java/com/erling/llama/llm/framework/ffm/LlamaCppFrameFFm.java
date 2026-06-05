package com.erling.llama.llm.framework.ffm;

import com.erling.core.load.ffm.api.cpp.clasz.NativeClass;
import com.erling.core.load.ffm.api.cpp.hook.AutoInit;
import com.erling.core.load.ffm.api.cpp.hook.CreatProxy;
import com.erling.core.load.ffm.api.cpp.hook.InitFunction;
import com.erling.llama.llm.backend.LlamaCallBack;
import com.erling.llama.llm.backend.LlamaInvokeBackEnd;
import com.erling.llama.llm.framework.ffm.struct.LlamaCtx;
import com.erling.llama.llm.framework.ffm.struct.LlamaParams;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;
import java.util.Map;
import java.util.function.Supplier;

public class LlamaCppFrameFFm implements NativeClass, LlamaInvokeBackEnd<LlamaCtx, LlamaParams> {


    Map<String,MethodHandle> functionMap;

    MemorySegment modelSegment;

    String modelPath;

    @CreatProxy(mappingClass = LlamaCppFFmInf.class)
    public LlamaCppFFmInf frameWorkInf;

    @Override
    @InitFunction
    public void setFunction(Map<String, MethodHandle> functionMap) {
          this.functionMap=functionMap;
    }

    @Override
    public MethodHandle getFunction(String name) {
        return functionMap.get(name);
    }

    @Override
    public Map<String, MethodHandle> getFunctionMap() {
        return functionMap;
    }

    @AutoInit
    public void AutoInit(String modelPath){
        this.modelPath = modelPath;
        if(frameWorkInf != null){
            init_model();
        }
    }

    @Override
    public void init_model() {
        try(Arena arena = Arena.ofShared()){
            modelSegment = frameWorkInf.
                    CreateLLm_GGuf_Framework(arena.allocateUtf8String(modelPath)
                    );
        }
    }

    @Override
    public void recording(
            String prompt, int eos,
            boolean clear, Supplier<LlamaCtx> ctx,
            Supplier<LlamaParams> params,
            LlamaCallBack callBack
    ) {
        RecodingKt.recording(
                this,
                prompt, eos,clear,ctx::get, params::get, callBack);
    }

    @Override
    public float[] embeddings(
            String prompt,boolean clear,
            Supplier<LlamaCtx> ctx,
            Supplier<LlamaParams> params
    ){
       return RecodingKt.embeddings(
                this,
                prompt,clear,
                ctx::get,
                params::get
        );
    }




    @Override
    public void destroy_model() {
        frameWorkInf.DestroyLLm_GGuf_Framework(modelSegment);
    }
}
