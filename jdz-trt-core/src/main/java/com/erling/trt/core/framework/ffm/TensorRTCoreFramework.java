package com.erling.trt.core.framework.ffm;

import com.erling.core.load.ffm.api.cpp.clasz.NativeClass;
import com.erling.core.load.ffm.api.cpp.hook.CreatProxy;

import java.lang.invoke.MethodHandle;
import java.util.Map;

public class TensorRTCoreFramework implements NativeClass {


    private Map<String, MethodHandle> functionMap;

    @CreatProxy(mappingClass = TensorRTCoreInf.class)
    public TensorRTCoreInf tensorRTCoreInf;

    @Override
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
}
