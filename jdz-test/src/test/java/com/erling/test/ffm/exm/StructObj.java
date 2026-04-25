package com.erling.test.ffm.exm;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.value.FieldInfo;
import com.erling.core.load.ffm.api.cpp.struct.value.TypeEnum;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static com.erling.core.load.ffm.api.cpp.struct.value.GetThisKt.getThis;
import static com.erling.core.load.ffm.api.cpp.struct.value.SetThisKt.setThis;


public class StructObj extends NativeStruct<StructObj> {

    public MemorySegment name;
    public int a;
    public int b;

    public StructObj(Arena arena) {
        super(StructObj.class, arena);
        System.out.println(fieldOffsetMap);
    }

    public StructObj setName(String value) {
        this.name = arena.allocateUtf8String(value);
        setThis(arena, () ->
                new FieldInfo(
                        TypeEnum.STRING,
                        fieldOffsetMap,
                        memorySegment,
                        "name",
                        this.name
                )
        );
        return this;
    }

    public StructObj setA(int a) {
        this.a = a;
        setThis(arena, () ->
                new FieldInfo(
                        TypeEnum.INT,
                        fieldOffsetMap,
                        memorySegment,
                        "a",
                        a
                )
        );
        return this;
    }

    public StructObj setB(int b) {
        this.b = b;
        setThis(arena, () ->
                new FieldInfo(
                        TypeEnum.INT,
                        fieldOffsetMap,
                        memorySegment,
                        "b",
                        b
                )
        );
        return this;
    }


    public String getName() {
        this.name = (MemorySegment) getThis(() ->
                new FieldInfo(
                        TypeEnum.ADDRESS,
                        fieldOffsetMap,
                        memorySegment,
                        "name",null
                )
        );
        return this.name.reinterpret(Long.MAX_VALUE).getUtf8String(0);
    }

    public int getA() {
        this.a=(int) getThis(() ->
                new FieldInfo(
                        TypeEnum.INT,
                        fieldOffsetMap,
                        memorySegment,
                        "a",null
                )
        );
        return this.a;
    }


    public int getB() {
        this.b = (int) getThis(() ->
                new FieldInfo(
                        TypeEnum.INT,
                        fieldOffsetMap,
                        memorySegment,
                        "b",null
                )
        );
        return this.b;
    }
}
