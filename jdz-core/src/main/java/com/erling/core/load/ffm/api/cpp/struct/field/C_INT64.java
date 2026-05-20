package com.erling.core.load.ffm.api.cpp.struct.field;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class C_INT64 extends C_FIELD<C_INT64> {

    public Long get() {
        return baseMemorySegment.get(ValueLayout.JAVA_LONG, offset);
    }

    public C_INT64 set(Long value) {
        baseMemorySegment.set(ValueLayout.JAVA_LONG, offset, value);
        return this;
    }

    public C_INT64(MemorySegment memorySegment, long offset, Arena arena) {
        super(memorySegment, offset, arena);
    }
}
