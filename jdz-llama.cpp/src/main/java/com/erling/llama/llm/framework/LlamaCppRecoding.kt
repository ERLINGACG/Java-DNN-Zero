package com.erling.llama.llm.framework

import com.erling.llama.llm.struct.LLM_GGUF_Batch
import com.erling.llama.llm.struct.LLM_GGUF_Context
import com.erling.llama.llm.struct.LLM_GGUF_Context_RTParam
import com.erling.llama.llm.struct.LLM_GGUF_Stream

fun interface RecCallback {
    fun invoke(token: String, count: Int): Boolean
}

fun recording(
    llamaCppFrameWork: LlamaCppFrameWork,
    prompt: String,
    eos: Int=-1,
    rtParmOpt: ()-> LLM_GGUF_Context_RTParam,
    ctxOpt:    ()-> LLM_GGUF_Context,
    recCallback: RecCallback
){
    val param = rtParmOpt.invoke()
    val ctx = ctxOpt.invoke()

    val pair =initBatch(llamaCppFrameWork, prompt, ctx)
    val ret = pair.first
    val batch = pair.second
    println(ret)
    initSampler(llamaCppFrameWork, param, ctx)

    if (eos != -1) {
        batch.eos = eos
    }
    var count=0

    do {
      val stream = LLM_GGUF_Stream()
      llamaCppFrameWork.frameWorkInf
          .ReasoningASync(
              llamaCppFrameWork.framework,
              batch,
              ctx,
              stream
          )
        count++
        if(!recCallback.invoke(stream.getStream(), count)){
            break
        }
    }while (batch.next_token != batch.eos)
    freeBatch(llamaCppFrameWork, batch)
}

fun initBatch(
    llamaCppFrameWork: LlamaCppFrameWork,
    prompt: String,
    ctx: LLM_GGUF_Context
) : Pair<Int, LLM_GGUF_Batch> {
    val batch = LLM_GGUF_Batch()
    val ret = llamaCppFrameWork.
        frameWorkInf.
            InitBatchASync(
                llamaCppFrameWork.framework,
                prompt.toByteArray(),
                batch,
                ctx
            )
    return Pair(ret, batch)
}

fun initSampler(
    llamaCppFrameWork: LlamaCppFrameWork,
    rtParam: LLM_GGUF_Context_RTParam,
    ctx: LLM_GGUF_Context

){
    llamaCppFrameWork.
        frameWorkInf.
            SetSamplerASync(
                llamaCppFrameWork.framework,
                rtParam,
                ctx
            )
}
fun freeBatch(
    llamaCppFrameWork: LlamaCppFrameWork,
    batch: LLM_GGUF_Batch
){
    llamaCppFrameWork.frameWorkInf.Batch_Free(batch)
}
