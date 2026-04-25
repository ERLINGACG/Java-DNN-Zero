package com.erling.test.ffm.exm;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.StructLayout;
import java.util.Map;

public interface CppLib {
    MemorySegment CreateCppToJava();

    void add(int a, int b);
    void PrintCppToJava(MemorySegment cppToJava);

    void SetStruct(MemorySegment structObj);

    void SetStruct2(MemorySegment structObj);

    void PrintCppStr(MemorySegment cppToJava, MemorySegment str);

    void PrintCppTestStruct(MemorySegment cppToJava);

    void PrintCppTestStruct2(MemorySegment cppToJava);
    void DestroyCppToJava(MemorySegment cppToJava);
}
