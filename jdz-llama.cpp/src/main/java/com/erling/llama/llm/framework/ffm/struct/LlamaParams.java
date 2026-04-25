package com.erling.llama.llm.framework.ffm.struct;

import com.erling.core.load.ffm.api.cpp.hook.Padding;
import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_FLOAT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

//struct LLM_GGUF_Context_RTParam {
//
//int top_k{};
//float top_p{};
//float temp{};
//
//int n_ctx{};
//int n_batch{};
//
//char* gbnf_str{};
//int  use_gbnf{};
//int  use_embeddings{};
//
//int     penalty_last_n{};   // last n tokens to penalize (0 = disable penalty, -1 = context size)
//float   penalty_repeat{};   // 1.0 = disabled
//float   penalty_freq{};     // 0.0 = disabled
//float   penalty_present{}; // 0.0 = disabled
//
//        };
public class LlamaParams extends NativeStruct<LlamaParams> {


    public  C_INT top_k; //0~3
    public  C_FLOAT top_p; //4~7
    public  C_FLOAT temp; //8~11
    public  C_INT n_ctx;//12~15
    public @Padding(4) C_INT n_batch;//16~19 20~23

    public C_POINTER  gbnf_str;    // 24~31
    public C_INT use_gbnf; // 32~35
    public C_INT use_embeddings; // 36~49

    public  C_INT penalty_last_n; // 40~43
    public  C_FLOAT penalty_repeat; // 44~47
    public  C_FLOAT penalty_freq; // 48~51
    public  C_FLOAT penalty_present; // 52~55

    public LlamaParams(Arena arena) {
        super(LlamaParams.class, arena);
        initFields(this);
        top_k.set(30);
        top_p.set(0.9f);
        temp.set(0.1f);
        n_ctx.set(1024);
        n_batch.set(1024);
        use_gbnf.set(0);
        use_embeddings.set(0);
        penalty_last_n.set(0);
        penalty_repeat.set(1.0f);
        penalty_freq.set(0.0f);
        penalty_present.set(0.0f);
    }
}
