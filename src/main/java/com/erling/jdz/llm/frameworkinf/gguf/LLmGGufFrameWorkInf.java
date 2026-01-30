package com.erling.jdz.llm.frameworkinf.gguf;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

//GDLZ_CORE_API gguf::LLm_GGuf_Framework* CreateLLm_GGuf_Framework(const char* conf_path);
//GDLZ_CORE_API void                      DestroyLLm_GGuf_Framework(const gguf::LLm_GGuf_Framework* framework);
public interface LLmGGufFrameWorkInf extends Library {

    Pointer CreateLLm_GGuf_Framework(String conf_path);

    void DestroyLLm_GGuf_Framework(Pointer framework);

    void ExampleSend(Pointer framework);
}
