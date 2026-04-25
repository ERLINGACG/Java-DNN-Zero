package com.erling.core.load.ffm.api.cpp.struct.field;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class C_INT extends C_FIELD<C_INT> {

    public C_INT(MemorySegment memorySegment, long offset, Arena arena){
        super(memorySegment, offset, arena);
    }

    public C_INT set(int value){
        baseMemorySegment.set(ValueLayout.JAVA_INT,offset,value);
        return this;
    }

    public int get() {
        return baseMemorySegment.get(ValueLayout.JAVA_INT,offset);
    }

}
