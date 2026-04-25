package com.erling.trt.rag.framework.ffm;

import com.erling.core.load.ffm.api.cpp.clasz.NativeClass;
import com.erling.core.load.ffm.api.cpp.hook.AutoInit;
import com.erling.core.load.ffm.api.cpp.hook.CreatProxy;
import com.erling.core.load.ffm.api.cpp.hook.InitFunction;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;
import java.util.List;
import java.util.Map;

public class TrtEmbFrameFFM implements NativeClass {

    Map<String, MethodHandle> functionMap;
    String modelPath;

    public MemorySegment modelSegment() {
        return modelSegment;
    }

    MemorySegment modelSegment;


    @CreatProxy(mappingClass = TrtEmbFrameInf.class)
    public TrtEmbFrameInf frameInf;

    @Override
    @InitFunction
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
        this.modelPath = modelPath;
        if(frameInf != null){
            init_model();
        }
    }

    public void init_model() {
        try(Arena arena = Arena.ofConfined()){
            modelSegment = frameInf.CreateTrtEmbFramework(arena.allocateUtf8String(modelPath));
        }
    }

//    public float[] forward(List<Integer> tokenizerIDs) {
////        return ForwardKt.forward(this, tokenizerIDs);
//    }
}
