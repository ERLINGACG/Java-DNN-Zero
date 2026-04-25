package com.erling.test.ffm.exm;

import java.lang.foreign.*;
import java.lang.invoke.VarHandle;

public class TestStruct {


    public StructLayout CPP_TEST_STRUCT = MemoryLayout.structLayout(
            ValueLayout.ADDRESS.withName("name"),
            ValueLayout.JAVA_INT.withName("a"),
            ValueLayout.JAVA_INT.withName("b")
    );

    private long NAME_OFFSET = CPP_TEST_STRUCT.byteOffset(MemoryLayout.PathElement.groupElement("name"));
    private long A_OFFSET = CPP_TEST_STRUCT.byteOffset(MemoryLayout.PathElement.groupElement("a"));
    private long B_OFFSET = CPP_TEST_STRUCT.byteOffset(MemoryLayout.PathElement.groupElement("b"));


    public MemorySegment createStruct(Arena arena, String name, int a, int b) {
        MemorySegment structSegment = arena.allocate(CPP_TEST_STRUCT);

        // 设置 name 字段：String → C 字符串
        MemorySegment cName = arena.allocateUtf8String(name);
        structSegment.set(ValueLayout.ADDRESS, NAME_OFFSET, cName);

        // 设置 int 字段
        structSegment.set(ValueLayout.JAVA_INT, A_OFFSET, a);
        structSegment.set(ValueLayout.JAVA_INT, B_OFFSET, b);

        return structSegment;
    }



}
