package com.erling.ort.emd.framework.ffm;

import com.erling.core.load.ffm.api.cpp.clasz.NativeClass;
import com.erling.core.load.ffm.api.cpp.hook.AutoInit;
import com.erling.core.load.ffm.api.cpp.hook.CreatProxy;
import com.erling.ort.emd.exce.OrtEmdInfIsNull;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;
import java.util.Map;

public class OnnxRtEmdFramework implements NativeClass {
    private Map<String, MethodHandle> functionMap;

    private String configPath;
    private MemorySegment emdFramework;

    @CreatProxy(mappingClass = OnnxRtEmdInf.class)
    public OnnxRtEmdInf emdInf;

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
            if (emdInf != null){
                emdFramework = emdInf.CreateEmdFramework(arena.allocateUtf8String(configPath));
            }else {
                throw new OrtEmdInfIsNull("OnnxRtEmdInf is null");
            }

        }
    }

    public MemorySegment emdFramework() {
        return emdFramework;
    }

    public String getConfigPath() {
        return configPath;
    }
}
