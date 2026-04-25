package com.erling.test.plugins.json.ffm;

import com.erling.core.load.ffm.api.cpp.clasz.NativeClass;
import com.erling.core.load.ffm.api.cpp.hook.CreatProxy;
import com.erling.core.load.ffm.api.cpp.hook.InitFunction;

import java.lang.invoke.MethodHandle;
import java.util.Map;

public class JPluginsCpp implements NativeClass {


    Map<String, MethodHandle> functionMap;

    @CreatProxy(mappingClass = JPluginsCppInf.class)
    public JPluginsCppInf jPluginsCppInf;

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
}
