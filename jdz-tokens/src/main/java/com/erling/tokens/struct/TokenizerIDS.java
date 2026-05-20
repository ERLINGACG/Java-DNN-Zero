package com.erling.tokens.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

public class TokenizerIDS  extends NativeStruct<TokenizerIDS> {
    public C_POINTER ids;

    public C_INT len;

    public TokenizerIDS(Arena arena){
        super(TokenizerIDS.class, arena);
        initFields(this);
    }

}
