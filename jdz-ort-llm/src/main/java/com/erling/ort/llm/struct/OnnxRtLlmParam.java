package com.erling.ort.llm.struct;

import com.erling.core.load.ffm.api.cpp.hook.Padding;
import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_FLOAT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT64;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

//struct OnnxRtLLmParam{
//int32_t top_k;        // top_k采样
//float_t top_p;       // top_p采样
//float_t temperature;// 温度
//int64_t seed;      // 随机种子
//    };
public class OnnxRtLlmParam extends NativeStruct<OnnxRtLlmParam> {

    public C_INT top_k;
    public C_FLOAT top_p;

    public @Padding(4) C_FLOAT temperature;
    public C_INT64 seed;
    public OnnxRtLlmParam(Arena arena) {
        super(OnnxRtLlmParam.class, arena);
        initFields(this);
        top_k.set(30);
        top_p.set(0.9f);
        temperature.set(0.7f);
        seed.set(System.currentTimeMillis());

    }
}
