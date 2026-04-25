package com.erling.core.load.ffm.api.cpp.struct;

import com.erling.core.load.ffm.api.cpp.hook.CreatLayOut;
import com.erling.core.load.ffm.api.cpp.hook.SetFieldsOffset;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.StructLayout;
import java.util.Map;
import java.util.function.Supplier;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.defaultScanStruct;
import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.registerStruct;

public  abstract class NativeStruct<T> {




    protected final Arena arena;

    protected MemorySegment memorySegment;
    protected StructLayout structLayout;
    public Map<String, Long> getFieldOffsetMap() {
        return fieldOffsetMap;
    }

    protected  Map<String, Long> fieldOffsetMap;

    public NativeStruct(Class<T> clazz, Arena arena) {
        this.arena = arena;
        registerStruct(clazz,this);
    }

    @CreatLayOut
    public void createStructLayout(StructLayout structLayout){
        this.structLayout = structLayout;
        memorySegment = arena.allocate(structLayout);
    }

    @SetFieldsOffset
    public void setFieldOffsetMap(Map<String, Long> fieldOffsetMap) {
        this.fieldOffsetMap = fieldOffsetMap;
    }

    public MemorySegment getMemorySegment() {
        return memorySegment;
    }

    public Arena getArena() {
        return arena;
    }

}
