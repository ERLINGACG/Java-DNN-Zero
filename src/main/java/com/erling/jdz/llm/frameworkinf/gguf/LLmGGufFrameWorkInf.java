package com.erling.jdz.llm.frameworkinf.gguf;

import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Batch;
import com.erling.jdz.llm.framework.gguf.stream.LLM_GGUF_Stream;
import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

//GDLZ_CORE_API gguf::LLm_GGuf_Framework* CreateLLm_GGuf_Framework(const char* conf_path);
//GDLZ_CORE_API void                      DestroyLLm_GGuf_Framework(const gguf::LLm_GGuf_Framework* framework);
//GDLZ_CORE_API int32_t                   SendExample(
//                                                const gguf::LLm_GGuf_Framework* framework,
//                                                gguf::batch::LLM_GGUF_Batch& batch,
//                                                data::LLM_GGUF_Stream& stream
//);
//        GDLZ_CORE_API void                     Batch_Free(gguf::batch::LLM_GGUF_Batch& batch);
public interface LLmGGufFrameWorkInf extends Library {

    Pointer CreateLLm_GGuf_Framework(String conf_path);

    void DestroyLLm_GGuf_Framework(Pointer framework);

    void ExampleSend(Pointer framework);

    int SendExample(Pointer framework,byte[] prompt,LLM_GGUF_Batch batch, LLM_GGUF_Stream stream);

    void InitBatch(Pointer framework,byte[] prompt,LLM_GGUF_Batch batch);

    void Reasoning(Pointer framework,LLM_GGUF_Batch batch, LLM_GGUF_Stream stream);

    void Batch_Free(LLM_GGUF_Batch batch);
}
