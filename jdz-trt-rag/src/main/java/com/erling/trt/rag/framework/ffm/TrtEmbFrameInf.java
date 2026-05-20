package com.erling.trt.rag.framework.ffm;

import java.lang.foreign.MemorySegment;
//GDLZ_CORE_API TrtEmbFramework* CreateTrtEmbFramework(const char* config_path);
//GDLZ_CORE_API void DestroyTrtEmbFramework(const TrtEmbFramework* framework);
//GDLZ_CORE_API void GetEngineInfo(const TrtEmbFramework* framework);
//GDLZ_CORE_API void CreateCtx(const TrtEmbFramework* framework, TrtEmbCtx& ctx);
//GDLZ_CORE_API void SetDims(const TrtEmbCtx& ctx, const char* name, const int* input, int len);
//GDLZ_CORE_API void Forward(const TrtEmbFramework* framework, int* input, int len,TrtEmbData& data);
public interface TrtEmbFrameInf {

    /**
     * 创建TensorRT嵌入框架实例
     * 
     * @param configPat 配置文件的路径，指向包含TensorRT引擎配置的文件
     * @return 指向新创建的TensorRT嵌入框架的MemorySegment指针
     * @throws RuntimeException 如果配置路径无效或框架创建失败
     */
    MemorySegment CreateTrtEmbFramework(MemorySegment configPat);

    /**
     * 获取TensorRT引擎信息
     * 
     * @param framework TensorRT嵌入框架的MemorySegment指针
     * @throws RuntimeException 如果框架指针无效或引擎信息获取失败
     */
    void GetEngineInfo(MemorySegment framework);

    /**
     * 创建嵌入上下文
     * 
     * @param framework TensorRT嵌入框架的MemorySegment指针
     * @param embCtx 用于接收创建的嵌入上下文的MemorySegment引用
     * @throws RuntimeException 如果框架指针无效或上下文创建失败
     */
    int CreateCtx(MemorySegment framework, MemorySegment embCtx);

    /**
     * 设置输入维度
     * 
     * @param ctx 嵌入上下文的MemorySegment指针
     * @param dims_name 维度名称的MemorySegment指针
     * @param dims 维度数组的MemorySegment指针
     * @param len 维度数组的长度
     * @throws RuntimeException 如果上下文指针无效或维度设置失败
     */
    int SetDims(MemorySegment ctx, MemorySegment dims_name, MemorySegment dims, int len);

    int GetDims(MemorySegment framework,MemorySegment ctx, MemorySegment dims_name);
//
    int PostCuda(MemorySegment framework,
                 MemorySegment ctx,
                 MemorySegment name,
                 MemorySegment data,
                 int len,int isMask
    );
//
    int Forward     (MemorySegment framework, MemorySegment ctx);
//
    int ForwardAsync(MemorySegment framework, MemorySegment ctx);
//
    int Synchronize(MemorySegment ctx);
//
    int GetEmbedding(MemorySegment framework,MemorySegment ctx, MemorySegment embedding,MemorySegment name);

    int GetPooledEmbedding(MemorySegment framework,
                           MemorySegment ctx,
                           MemorySegment embedding,
                           MemorySegment hidden_state_name,
                           MemorySegment attention_mask_name
    );

    int ClearBindings(MemorySegment ctx);

}
