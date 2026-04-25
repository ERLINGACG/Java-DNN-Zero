package com.erling.test.ffm.exm;

import com.erling.core.load.ffm.api.cpp.clasz.NativeClass;
import com.erling.core.load.ffm.api.cpp.hook.CreatProxy;
import com.erling.core.load.ffm.api.cpp.hook.InitFunction;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;
import java.util.Map;

import static com.erling.core.load.ffm.api.loader.FFmLoaderKt.createNativeProxyForJava;

public class TestNaClass implements NativeClass {


    Map<String, MethodHandle> functionMap;



    @CreatProxy(mappingClass = CppLib.class)
    public CppLib cppLib;

    MemorySegment cppToJava;

    @Override
    @InitFunction
    public void setFunction(Map<String, MethodHandle> functionMap) {
        this.functionMap = functionMap;
    }


    public void CreateProxy(Class<?> mappingClass, Map<String, MethodHandle> functionMap) {
        cppLib = (CppLib) createNativeProxyForJava(mappingClass,functionMap);
    }

    @Override
    public MethodHandle getFunction(String name) {
        return functionMap.get(name);
    }

    @Override
    public Map<String, MethodHandle> getFunctionMap() {
        return functionMap;
    }

    public void CreateCppToJava() {
        cppToJava = cppLib.CreateCppToJava();
    }



    public void PrintCppToJava() {
        cppLib.PrintCppToJava(cppToJava);
    }

    public void PrintCppStr(String str) {
        try (Arena arena = Arena.ofConfined()) {
            cppLib.PrintCppStr(cppToJava,  arena.allocateUtf8String(str));
        }
    }

    public void PrintCppTestStruct(MemorySegment structSegment) {
        cppLib.PrintCppTestStruct(structSegment);
    }

    public void SetStruct(MemorySegment structObj) {
        cppLib.SetStruct(structObj);
    }
    public void DestroyCppToJava() {
        cppLib.DestroyCppToJava(cppToJava);
    }

    public void PrintCppTestStruct2(MemorySegment structSegment) {
        cppLib.PrintCppTestStruct2(structSegment);
    }
    public void SetStruct2(MemorySegment structObj) {
        cppLib.SetStruct2(structObj);
    }

}
