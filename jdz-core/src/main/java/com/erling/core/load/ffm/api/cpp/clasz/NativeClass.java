package com.erling.core.load.ffm.api.cpp.clasz;

import java.lang.invoke.MethodHandle;
import java.util.Map;

public interface NativeClass {

    void setFunction(Map<String, MethodHandle> functionMap);

    MethodHandle getFunction(String name);

    Map<String, MethodHandle> getFunctionMap();



}
