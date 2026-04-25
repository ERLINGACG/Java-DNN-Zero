package com.erling.test.ffm.exm;

import com.erling.core.load.ffm.api.cpp.hook.FieldMapping;
import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

public class StructObjV2 extends NativeStruct<StructObjV2> {


    @FieldMapping(name = "name")
    public C_POINTER name;

    @FieldMapping(name = "a")
    public C_INT a;

    @FieldMapping(name = "b")
    public C_INT b;


    public StructObjV2(Arena arena) {
        super(StructObjV2.class, arena);
        initFields(this);
    }


}
