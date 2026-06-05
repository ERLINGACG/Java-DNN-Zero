package com.erling.ort.llm.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT64;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

/**
     struct OnnxRtLLmCtx {
         std::unique_ptr<std::vector<char*>> input_names_ptr;
         std::unique_ptr<std::vector<char*>> output_names_ptr;
         int32_t layers;
         int32_t heards;
         int32_t head_dim;
         int32_t batch;
         int64_t token_pos;
         int64_t next_token_id;

         ~OnnxRtLLmCtx()=default;
     };
 * */
public class OnnxRtLlmCtx extends NativeStruct<OnnxRtLlmCtx> {


    public C_POINTER memory_ptr;

    public C_POINTER sampler;

    public C_INT64 token_pos;

    public C_INT64 next_token_id;

    public C_INT batch;


    public OnnxRtLlmCtx(Arena arena) {
        super(OnnxRtLlmCtx.class, arena);
        initFields(this);
        this.batch.set(1);
    }

}
