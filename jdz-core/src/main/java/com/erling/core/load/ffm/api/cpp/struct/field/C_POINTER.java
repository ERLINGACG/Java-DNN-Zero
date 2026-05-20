package com.erling.core.load.ffm.api.cpp.struct.field;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.charset.StandardCharsets;

public class C_POINTER extends C_FIELD<C_POINTER> {


    public C_POINTER(MemorySegment memorySegment, long offset, Arena arena){
        super(memorySegment, offset, arena);
    }

    public C_POINTER set(String value){
        baseMemorySegment.set(ValueLayout.ADDRESS,offset,arena.allocateUtf8String(value));
        return this;
    }

    public C_POINTER set(int[] value){
        baseMemorySegment.set(ValueLayout.ADDRESS,offset,arena.allocateArray(ValueLayout.JAVA_INT,value));
        return this;
    }
    public C_POINTER set(short[] value){
        baseMemorySegment.set(ValueLayout.ADDRESS,offset,arena.allocateArray(ValueLayout.JAVA_SHORT,value));
        return this;
    }

    public C_POINTER set(float[] value){
        baseMemorySegment.set(ValueLayout.ADDRESS,offset,arena.allocateArray(ValueLayout.JAVA_FLOAT,value));
        return this;
    }

    public C_POINTER set(long[] value){
        baseMemorySegment.set(ValueLayout.ADDRESS,offset,arena.allocateArray(ValueLayout.JAVA_LONG,value));
        return this;
    }

    public String getForString(){
        return baseMemorySegment.
                get(ValueLayout.ADDRESS, offset).
                reinterpret(Long.MAX_VALUE).
                getUtf8String(0);
    }

    public String getForString(long len){
        MemorySegment ptr = baseMemorySegment.get(ValueLayout.ADDRESS, offset);
        if (ptr.address() == 0) return null;
        return new String(ptr.reinterpret(len).toArray(ValueLayout.JAVA_BYTE), StandardCharsets.UTF_8);
    }

    public float[] getForFloatArray(long len) {
        MemorySegment ptr = baseMemorySegment.get(ValueLayout.ADDRESS, offset);
        if (ptr.address() == 0) return null;
        return ptr.reinterpret(len * 4).toArray(ValueLayout.JAVA_FLOAT);
    }

    public int[] getForIntArrayArray(long len) {
        MemorySegment ptr = baseMemorySegment.get(ValueLayout.ADDRESS, offset);
        if (ptr.address() == 0) return null;
        return ptr.reinterpret(len * 4).toArray(ValueLayout.JAVA_INT);
    }

    public long[] getForLongArray(long len) {
        MemorySegment ptr = baseMemorySegment.get(ValueLayout.ADDRESS, offset);
        if (ptr.address() == 0) return null;
        return ptr.reinterpret(len * 8).toArray(ValueLayout.JAVA_LONG);
    }


}
