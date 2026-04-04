package com.erling.jdz.llm.framework.gguf.batch;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * <h3>GGUF 上下文运行时参数</h3>
 * <p>对应 C 结构体 {@code struct LLM_GGUF_Context_RTParam}，用于配置模型推理时的采样、惩罚等行为。</p>
 *
 * <p><b>字段分组说明：</b></p>
 * <ul>
 *   <li><b>采样参数</b> - top_k, top_p, temp</li>
 *   <li><b>上下文参数</b> - n_ctx, n_batch</li>
 *   <li><b>GBNF 语法</b> - gbnf_str, use_gbnf, use_embeddings</li>
 *   <li><b>惩罚参数</b> - penalty_last_n, penalty_repeat, penalty_freq, penalty_present</li>
 * </ul>
 */
@Structure.FieldOrder({
        "top_k", "top_p", "temp",
        "n_ctx", "n_batch",
        "gbnf_str", "use_gbnf", "use_embeddings",
        "penalty_last_n", "penalty_repeat", "penalty_freq", "penalty_present"
})
public class LLM_GGUF_Context_RTParam extends Structure {
    public int top_k=30;
    public float top_p=0.9f;
    public float temp=0.1f;

    public int n_ctx=1024;
    public int n_batch=1024;

    public String gbnf_str;
    public boolean use_gbnf=false;
    public int use_embeddings=0;

    public int penalty_last_n=0;
    public float penalty_repeat=1.0f;
    public float penalty_freq=0.0f;
    public float penalty_present=0.0f;

}
