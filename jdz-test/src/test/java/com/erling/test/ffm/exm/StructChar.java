package com.erling.test.ffm.exm;

import com.erling.core.load.ffm.api.cpp.struct.CArray;
import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_ARRAY;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

public class StructChar extends NativeStruct<StructChar> {

    @CArray(len = 100)
    public C_ARRAY name;

    public C_INT a;

    public StructChar(Arena arena) {
        super(StructChar.class, arena);
        initFields(this);
        System.out.println(structLayout);
    }

//    public StructChar setName(String name) {
//        setThisForCArray(memorySegment, 0, name,100);
//        return this;
//    }
//
//    public String getName() {
//        return getThisForCArray(memorySegment, 0, 100);
//    }
}
