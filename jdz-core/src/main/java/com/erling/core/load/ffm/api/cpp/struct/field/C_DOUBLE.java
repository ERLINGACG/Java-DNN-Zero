package com.erling.core.load.ffm.api.cpp.struct.field;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class C_DOUBLE extends C_FIELD<C_DOUBLE> {
    public C_DOUBLE(MemorySegment memorySegment, long offset, Arena arena){
        super(memorySegment, offset, arena);
    }

    public C_DOUBLE set(double value){
        baseMemorySegment.set(ValueLayout.JAVA_DOUBLE, offset,value);
        return this;
    }

    public double get(){
        return baseMemorySegment.get(ValueLayout.JAVA_DOUBLE, offset);
    }
}
