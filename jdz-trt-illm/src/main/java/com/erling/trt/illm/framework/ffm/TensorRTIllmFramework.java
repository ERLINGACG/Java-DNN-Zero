package com.erling.trt.illm.framework.ffm;

import com.erling.core.load.ffm.api.cpp.clasz.NativeClass;
import com.erling.core.load.ffm.api.cpp.hook.AutoInit;
import com.erling.core.load.ffm.api.cpp.hook.CreatProxy;
import com.erling.core.load.ffm.api.cpp.hook.InitFunction;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;
import java.util.Map;

public class TensorRTIllmFramework implements NativeClass {
    private Map<String, MethodHandle> functionMap;

    @CreatProxy(mappingClass = TensorRTIllmInf.class)
    public TensorRTIllmInf tensorRTIllmInf;

    public MemorySegment framework;

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
    public void init(String configPath){
        try(Arena arena = Arena.ofShared()){
            framework = tensorRTIllmInf.CreateTensorRTIllmFramework(
                    arena.allocateUtf8String(configPath)
            );
            if(tensorRTIllmInf.CreateTensorRTIllmEngine(framework)!=0){
                throw new RuntimeException("CreateTensorRTIllmEngine failed");
            }
        }
    }

    public void destroy(){
        tensorRTIllmInf.DestroyTensorRTIllmFramework(framework);
    }
}
