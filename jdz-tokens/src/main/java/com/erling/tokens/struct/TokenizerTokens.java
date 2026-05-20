package com.erling.tokens.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

public class TokenizerTokens extends NativeStruct<TokenizerTokens> {
       public C_POINTER tokens;
        public C_INT len;
        public TokenizerTokens(Arena arena){
            super(TokenizerTokens.class, arena);
            initFields(this);
        }
}
