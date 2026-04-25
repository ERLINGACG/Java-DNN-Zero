package com.erling.core.load.ffm.api.cpp.struct.field;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class C_FLOAT extends C_FIELD<C_FLOAT> {
    public C_FLOAT(MemorySegment memorySegment, long offset, Arena arena){
        super(memorySegment, offset, arena);
    }

    public C_FLOAT set(float value){
        baseMemorySegment.set(ValueLayout.JAVA_FLOAT,offset,value);
        return this;
    }

    public float get() {
        return baseMemorySegment.get(ValueLayout.JAVA_FLOAT,offset);
    }
}
