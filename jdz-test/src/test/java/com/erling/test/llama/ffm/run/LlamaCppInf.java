package com.erling.test.llama.ffm.run;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.util.Map;

public interface LlamaCppInf {
    MemorySegment CreateLLm_GGuf_Framework(MemorySegment conf_path);

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
}
