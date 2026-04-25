package com.erling.core.load.ffm.api.cpp.struct.field;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class C_ARRAY extends C_FIELD<C_ARRAY> {

    public long len;
    public C_ARRAY(MemorySegment memorySegment, long offset, Arena arena, long len) {
        super(memorySegment, offset, arena);
        this.len = len;
    }

    public C_ARRAY set(String value){
        baseMemorySegment.asSlice(offset, len).fill((byte) 0).setUtf8String(0, value);
        return this;
    }
    public C_ARRAY set(byte[] value){
        for (int i = 0; i < value.length && i < len; i++) {
            baseMemorySegment.set(ValueLayout.JAVA_BYTE, offset + i, value[i]);
        }
        if (value.length < len) {
            baseMemorySegment.set(ValueLayout.JAVA_BYTE, offset + value.length, (byte) 0);
        }
        return this;
    }

    public String getString() {
        return baseMemorySegment.asSlice(offset, len).getUtf8String(0);
    }
}
