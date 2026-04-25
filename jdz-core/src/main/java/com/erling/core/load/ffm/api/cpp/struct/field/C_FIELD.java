package com.erling.core.load.ffm.api.cpp.struct.field;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

public abstract class C_FIELD<T extends C_FIELD<T>> {
    protected MemorySegment baseMemorySegment;

    protected Arena arena;

    public Long offset() {
        return offset;
    }

    protected Long offset;

    public C_FIELD(MemorySegment memorySegment, Long offset, Arena arena){
        this.baseMemorySegment = memorySegment;
        this.offset = offset;
        this.arena = arena;
    }
}
