package com.erling.llama.llm.framework.jna

import com.erling.llama.llm.backend.LlamaCallBack
import com.erling.llama.llm.framework.jna.struct.LLM_GGUF_Batch
import com.erling.llama.llm.framework.jna.struct.LLM_GGUF_Context
import com.erling.llama.llm.framework.jna.struct.LLM_GGUF_Context_RTParam
import com.erling.llama.llm.framework.jna.struct.LLM_GGUF_Stream

fun interface RecCallback {
    fun invoke(token: String, count: Int, tokenizer : Int): Boolean
}

fun recording(
    llamaCppFrameWorkJNA: LlamaCppFrameWorkJNA,
    prompt: String,
    eos: Int=-1,
    rtParmOpt: ()-> LLM_GGUF_Context_RTParam,
    ctxOpt:    ()-> LLM_GGUF_Context,
    callBack: LlamaCallBack
){
    val param = rtParmOpt.invoke()
    val ctx = ctxOpt.invoke()

    val pair =initBatch(llamaCppFrameWorkJNA, prompt, ctx)
    val tokenizer = pair.first
    val batch = pair.second
//    println(ret)
    if(tokenizer>param.n_ctx){
        throw IllegalArgumentException("prompt length is too long")
    }
    initSampler(llamaCppFrameWorkJNA, param, ctx)

    if (eos != -1) {
        batch.eos = eos
    }
    var count=0

    do {
      val stream = LLM_GGUF_Stream()
      llamaCppFrameWorkJNA.frameWorkInf
          .ReasoningASync(
              llamaCppFrameWorkJNA.framework,
              batch,
              ctx,
              stream
          )
        count++
        if(!callBack.invoke(stream.getStream(), count, tokenizer)){
            break
        }
    }while (batch.next_token != batch.eos)
    freeBatch(llamaCppFrameWorkJNA, batch)
}

fun initBatch(
    llamaCppFrameWorkJNA: LlamaCppFrameWorkJNA,
    prompt: String,
    ctx: LLM_GGUF_Context
) : Pair<Int, LLM_GGUF_Batch> {
    val batch = LLM_GGUF_Batch()
    val ret = llamaCppFrameWorkJNA.
        frameWorkInf.
            InitBatchASync(
                llamaCppFrameWorkJNA.framework,
                prompt.toByteArray(),
                batch,
                ctx
            )
    return Pair(ret, batch)
}

fun initSampler(
    llamaCppFrameWorkJNA: LlamaCppFrameWorkJNA,
    rtParam: LLM_GGUF_Context_RTParam,
    ctx: LLM_GGUF_Context

){
    llamaCppFrameWorkJNA.
        frameWorkInf.
            SetSamplerASync(
                llamaCppFrameWorkJNA.framework,
                rtParam,
                ctx
            )
}
fun freeBatch(
    llamaCppFrameWorkJNA: LlamaCppFrameWorkJNA,
    batch: LLM_GGUF_Batch
){
    llamaCppFrameWorkJNA.frameWorkInf.Batch_Free(batch)
}
