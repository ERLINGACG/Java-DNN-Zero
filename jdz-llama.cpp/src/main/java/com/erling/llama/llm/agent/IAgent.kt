package com.erling.llama.llm.agent

import com.erling.llama.llm.backend.LlamaCallBack
import com.erling.llama.llm.backend.LlamaInvokeBackEnd
import com.erling.llama.llm.prompt.IPromptTemplate
import java.util.function.Supplier

interface IAgent<T: IAgent<T,C,P>,C,P> {

    fun setFrameWork(frameWork: LlamaInvokeBackEnd<C, P>): T
    fun setTemplate(template: IPromptTemplate): T

    fun chat(system: String,prompt: String,clear: Boolean=false,
             rtSupplier:Supplier<C> ,
             ctxSupplier:Supplier<P> ,
             recCallBack: LlamaCallBack
    ):T
    fun chat(prompt: String,clear: Boolean=false,
             rtSupplier:Supplier<C> ,
             ctxSupplier:Supplier<P> ,
             recCallBack: LlamaCallBack
    ):T








}