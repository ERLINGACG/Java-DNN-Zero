package com.erling.test.ort.test;

import com.erling.core.load.ffm.api.cpp.clasz.NativeClass;
import com.erling.core.load.ffm.api.cpp.hook.CreatProxy;

import java.lang.invoke.MethodHandle;
import java.util.Map;

public class OnnxRTExample implements NativeClass {
    private Map<String, MethodHandle> functionMap;
    @Override
    public void setFunction(Map<String, MethodHandle> functionMap) {
        this.functionMap = functionMap;
    }

    @CreatProxy(mappingClass = OnnxRTInf.class)
    public OnnxRTInf onnxRTInf;

    @Override
    public MethodHandle getFunction(String name) {
        return functionMap.get(name);
    }

    @Override
    public Map<String, MethodHandle> getFunctionMap() {
        return functionMap;
    }
}
