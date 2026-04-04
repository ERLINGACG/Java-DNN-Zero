package com.erling.llama.llm.struct;

import com.sun.jna.Structure;

/**
 * <h3>LLM GGUF 上下文运行时参数结构体</h3>
 * 
 * <p>该结构体定义了LLM模型推理时的运行时参数配置。</p>
 * 
 * <h4>采样参数：</h4>
 * <ul>
 *   <li><b>top_k</b> - Top-K采样参数，限制候选token数量</li>
 *   <li><b>top_p</b> - Top-P采样参数，基于概率累积的采样阈值</li>
 *   <li><b>temp</b> - 温度参数，控制生成随机性</li>
 * </ul>
 * 
 * <h4>上下文配置：</h4>
 * <ul>
 *   <li><b>n_ctx</b> - 上下文窗口大小</li>
 *   <li><b>n_batch</b> - 批处理大小</li>
 * </ul>
 * 
 * <h4>GBNF语法配置：</h4>
 * <ul>
 *   <li><b>gbnf_str</b> - GBNF语法字符串指针</li>
 *   <li><b>use_gbnf</b> - 是否使用GBNF语法约束</li>
 *   <li><b>use_embeddings</b> - 是否使用嵌入向量</li>
 * </ul>
 * 
 * <h4>重复惩罚参数：</h4>
 * <ul>
 *   <li><b>penalty_last_n</b> - 最后n个token进行惩罚（0=禁用惩罚，-1=上下文大小）</li>
 *   <li><b>penalty_repeat</b> - 重复惩罚系数（1.0=禁用）</li>
 *   <li><b>penalty_freq</b> - 频率惩罚系数（0.0=禁用）</li>
 *   <li><b>penalty_present</b> - 存在惩罚系数（0.0=禁用）</li>
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