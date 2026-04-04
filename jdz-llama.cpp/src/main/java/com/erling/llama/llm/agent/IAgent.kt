package com.erling.llama.llm.agent

import com.erling.llama.llm.framework.LlamaCppFrameWork
import com.erling.llama.llm.framework.RecCallback
import com.erling.llama.llm.prompt.IPromptTemplate
import com.erling.llama.llm.struct.LLM_GGUF_Context
import com.erling.llama.llm.struct.LLM_GGUF_Context_RTParam
import java.util.function.Supplier

interface IAgent<T: IAgent<T>> {

    fun setFrameWork(frameWork: LlamaCppFrameWork): T
    fun setTemplate(template: IPromptTemplate): T

    fun chat(system: String,prompt: String,
             rtSupplier:Supplier<LLM_GGUF_Context_RTParam> ,
             ctxSupplier:Supplier<LLM_GGUF_Context> ,
             recCallback: RecCallback
    ):T
    fun chat(prompt: String,
             rtSupplier:Supplier<LLM_GGUF_Context_RTParam> ,
             ctxSupplier:Supplier<LLM_GGUF_Context> ,
             recCallback: RecCallback
    ):T





}