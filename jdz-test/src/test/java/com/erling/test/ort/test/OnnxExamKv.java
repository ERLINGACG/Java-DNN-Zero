package com.erling.test.ort.test;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

public class OnnxExamKv extends NativeStruct<OnnxExamKv> {

    public C_POINTER next_past_kv;

    public C_INT org_token_len;
    public C_INT token_count;
    public OnnxExamKv(Arena arena) {
        super(OnnxExamKv.class, arena);
        initFields(this);
    }
}
