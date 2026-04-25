package com.erling.llama.llm.framework.ffm;

import java.lang.foreign.MemorySegment;

public interface LlamaCppFFmInf {
    MemorySegment CreateLLm_GGuf_Framework(MemorySegment conf_path);

    void DestroyLLm_GGuf_Framework(MemorySegment framework);

    void SetSamplerASync(MemorySegment framework,MemorySegment params, MemorySegment ctx);

    //    int gdlz::llm::gguf_export::InitBatchASync(
//        const gguf::LLm_GGuf_Framework* framework,
//        const char* prompt,
//        gguf::batch::LLM_GGUF_Batch& batch,
//        gguf::batch::LLM_GGUF_Context& context
//    )
    int InitBatchASync(MemorySegment framework,MemorySegment prompt,MemorySegment batch,MemorySegment context);
    //    void gdlz::llm::gguf_export::ReasoningASync(
//        const gguf::LLm_GGuf_Framework* framework,
//        gguf::batch::LLM_GGUF_Batch& batch,
//        gguf::batch::LLM_GGUF_Context& context,
//        data::LLM_GGUF_Stream& stream
//    )
    void ReasoningASync(
            MemorySegment framework,
            MemorySegment batch,
            MemorySegment context,
            MemorySegment stream
    );
    void Batch_Free(MemorySegment batch);

    void InitEmbeddings(
            MemorySegment framework,
            MemorySegment prompt,
            MemorySegment batch,
            MemorySegment context
    );
    void GetEmbeddings(
            MemorySegment framework,
            MemorySegment batch,
            MemorySegment context,
            MemorySegment embeddings
    );

    void Embedding_Free(MemorySegment embeddings);

    void Context_Free(MemorySegment context);
}
