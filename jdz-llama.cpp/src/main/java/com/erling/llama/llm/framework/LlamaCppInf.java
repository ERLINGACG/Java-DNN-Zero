package com.erling.llama.llm.framework;

import com.erling.llama.llm.struct.LLM_GGUF_Batch;
import com.erling.llama.llm.struct.LLM_GGUF_Context;
import com.erling.llama.llm.struct.LLM_GGUF_Context_RTParam;
import com.erling.llama.llm.struct.LLM_GGUF_Stream;
import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 * <h3>Llama.cpp JNA 接口定义</h3>
 * 
 * <p>该接口定义了与Llama.cpp C++库交互的JNA方法映射。</p>
 * 
 * <h4>框架生命周期管理：</h4>
 * <ul>
 *   <li><b>CreateLLm_GGuf_Framework</b> - 创建LLM GGUF框架实例</li>
 *   <li><b>DestroyLLm_GGuf_Framework</b> - 销毁LLM GGUF框架实例</li>
 * </ul>
 * 
 * <h4>同步推理功能：</h4>
 * <ul>
 *   <li><b>SetSamplerRT</b> - 设置实时采样器参数（GBNF语法、top_k、top_p、温度等）</li>
 *   <li><b>InitBatch</b> - 初始化批处理数据</li>
 *   <li><b>Reasoning</b> - 执行推理过程</li>
 *   <li><b>Batch_Free</b> - 释放批处理资源</li>
 * </ul>
 * 
 * <h4>异步推理功能：</h4>
 * <ul>
 *   <li><b>SetSamplerASync</b> - 设置异步采样器参数</li>
 *   <li><b>InitBatchASync</b> - 异步初始化批处理</li>
 *   <li><b>ReasoningASync</b> - 异步执行推理</li>
 * </ul>
 * 
 * <h4>嵌入向量功能：</h4>
 * <ul>
 *   <li><b>InitEmbeddings</b> - 初始化嵌入向量计算</li>
 *   <li><b>GetEmbeddings</b> - 获取嵌入向量结果</li>
 * </ul>
 * 
 * <h4>工具函数：</h4>
 * <ul>
 *   <li><b>Json_ExampleSend</b> - JSON示例发送函数</li>
 * </ul>
 * 
 * <p><b>注意：</b>所有方法都映射到C++库中的对应函数，参数类型和返回值需要与C++端保持一致。</p>
 */
public interface LlamaCppInf extends Library {
    Pointer CreateLLm_GGuf_Framework(String conf_path);

    void DestroyLLm_GGuf_Framework(Pointer framework);

    void InitBatch(Pointer framework, byte[] prompt, LLM_GGUF_Batch batch);

    void Reasoning(Pointer framework,LLM_GGUF_Batch batch, LLM_GGUF_Stream stream);

    void Batch_Free(LLM_GGUF_Batch batch);

    void SetSamplerASync(Pointer framework, LLM_GGUF_Context_RTParam param, LLM_GGUF_Context context);

    int InitBatchASync(Pointer framework, byte[] prompt, LLM_GGUF_Batch batch, LLM_GGUF_Context context);

    void ReasoningASync(Pointer framework, LLM_GGUF_Batch batch, LLM_GGUF_Context context, LLM_GGUF_Stream stream);
}