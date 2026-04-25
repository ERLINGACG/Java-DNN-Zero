package com.erling.test.plugins.json.ffm;

import com.erling.core.load.ffm.api.cpp.hook.Padding;
import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

//struct JsonNodeInfo
//    {
//std::unique_ptr<char[]> key{};
//int key_len{};
//
//std::unique_ptr<char[]> value{};
//int value_len{};
//
//std::unique_ptr<char[]> type{};
//int type_len{};
//
//int level = 0;
//
//    };
public class InfoStruct extends NativeStruct<InfoStruct> {

    public C_POINTER key; // 0

    public @Padding(4) C_INT key_len; // 8


    public C_POINTER value; // 16

    public @Padding(4) C_INT value_len; // 24

    public C_POINTER type;  //32

    public C_INT type_len; //36

    public C_INT level;     //44

    public InfoStruct(Arena arena) {
        super(InfoStruct.class, arena);
//        initFields(this);

    }
}
