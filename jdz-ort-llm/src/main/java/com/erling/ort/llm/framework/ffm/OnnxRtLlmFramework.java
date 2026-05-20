package com.erling.ort.llm.framework.ffm;

import com.erling.core.load.ffm.api.cpp.clasz.NativeClass;
import com.erling.core.load.ffm.api.cpp.hook.AutoInit;
import com.erling.core.load.ffm.api.cpp.hook.CreatProxy;
import com.erling.ort.llm.exce.OrtLLmFrameInfIsNull;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;
import java.util.Map;

public class OnnxRtLlmFramework implements NativeClass {

    String configPath;

    public MemorySegment framework() {
        return framework;
    }

    MemorySegment framework;

    @CreatProxy(mappingClass = OnnxRtLlmInf.class)
    public OnnxRtLlmInf onnxRtLlmInf;

    private Map<String, MethodHandle> functionMap;
    @Override
    public void setFunction(Map<String, MethodHandle> functionMap) {
        this.functionMap = functionMap;
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
        this.configPath = modelPath;
        try(Arena arena = Arena.ofConfined()){
            if (onnxRtLlmInf != null){
                framework = onnxRtLlmInf.
                        CreateLLmFramework(arena.allocateUtf8String(configPath));
            }else {
                throw new OrtLLmFrameInfIsNull("OnnxRtLlmInf is null");
            }

        }
    }
}
