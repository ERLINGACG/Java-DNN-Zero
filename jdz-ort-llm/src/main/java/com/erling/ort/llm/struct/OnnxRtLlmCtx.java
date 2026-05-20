package com.erling.ort.llm.struct;

import com.erling.core.load.ffm.api.cpp.hook.Padding;
import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT64;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    private final List<Long> memory_tokens = new ArrayList<>();

    public OnnxRtLlmCtx(Arena arena) {
        super(OnnxRtLlmCtx.class, arena);
        initFields(this);
        this.batch.set(1);
    }

    public OnnxRtLlmCtx addMemoryTokens(long[] memoryTokens) {
        Arrays.stream(memoryTokens).boxed().forEach(memory_tokens::add);
        return this;
    }

    public long[] getMemoryTokens() {
        return memory_tokens.stream().mapToLong(Long::valueOf).toArray();
    }
    public long[] generateAttentionMask() {
        var mask=new long[getMemoryTokens().length];
        Arrays.fill(mask,1L);
        return mask;
    }
    public long[] generatePositionIds() {
        var ids=new long[getMemoryTokens().length];
        if(this.token_pos.get()==0){
            for(int i=0;i<ids.length;++i){
                ids[i]=i;
            }
        }else{
            for (int i = 0; i < ids.length; ++i) {
                ids[i] = i + this.token_pos.get();
            }
        }
        this.token_pos.set(this.token_pos.get()+getMemoryTokens().length);
        return ids;
    }
}
