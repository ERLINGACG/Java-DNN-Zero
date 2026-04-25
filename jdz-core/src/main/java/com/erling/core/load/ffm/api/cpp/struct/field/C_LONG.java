package com.erling.core.load.ffm.api.cpp.struct.field;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class C_LONG extends C_FIELD<C_LONG> {


    public Long get() {
        return baseMemorySegment.get(ValueLayout.JAVA_LONG, offset);
    }

    public C_LONG set(Long value) {
        baseMemorySegment.set(ValueLayout.JAVA_LONG, offset, value);
        return this;
    }

    public C_LONG(MemorySegment memorySegment, Long offset, Arena arena) {
        super(memorySegment, offset, arena);
    }
}
