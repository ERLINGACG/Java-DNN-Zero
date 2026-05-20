package com.erling.llama.llm.framework.ffm

import com.erling.llama.llm.backend.LlamaCallBack
import com.erling.llama.llm.framework.ffm.struct.LlamaBatch
import com.erling.llama.llm.framework.ffm.struct.LlamaCtx
import com.erling.llama.llm.framework.ffm.struct.LlamaEmb
import com.erling.llama.llm.framework.ffm.struct.LlamaParams
import com.erling.llama.llm.framework.ffm.struct.LlamaStream
import java.lang.foreign.Arena
import java.lang.foreign.MemorySegment
import java.lang.foreign.ValueLayout
import kotlin.math.min

fun recording(
    llamaCppFrameWorkFFM: LlamaCppFrameFFm,
    prompt: String,
    eos: Int=-1,
    clear: Boolean=false,
    ctxOpt:    ()-> LlamaCtx,
    rtParmOpt: ()-> LlamaParams,
    callBack: LlamaCallBack
){
    Arena.ofConfined().use { arena ->
       val batch = LlamaBatch(arena)
       val ctx = ctxOpt.invoke()
       val params = rtParmOpt.invoke()

       val tokenizer = llamaCppFrameWorkFFM.frameWorkInf.InitBatchASync(
           llamaCppFrameWorkFFM.modelSegment,
           arena.allocateUtf8String(prompt),
           batch.memorySegment,
           ctx.memorySegment
       )

       llamaCppFrameWorkFFM.frameWorkInf.SetSamplerASync(
           llamaCppFrameWorkFFM.modelSegment,
           params.memorySegment,ctx.memorySegment
       )
       var map = HashMap<Int,String>()
       val n_eos = if (eos > 0) eos else batch.eos.get()
       var count=0
       do{
            val stream = LlamaStream(arena)
            val start_time = System.currentTimeMillis()
            llamaCppFrameWorkFFM.frameWorkInf.ReasoningASync(
                llamaCppFrameWorkFFM.modelSegment,
                batch.memorySegment,
                ctx.memorySegment,
                stream.memorySegment
            )
            val end_time = System.currentTimeMillis()
            if(!callBack.invoke(
                    stream.stream.string,
                    count,
                    tokenizer)
            ){
                break
            }
            count++
            map[count] = "${end_time-start_time}ms"

       }while (batch.next_token.get() != n_eos)
       println()
       println(map)
       llamaCppFrameWorkFFM.frameWorkInf.Batch_Free(batch.memorySegment)
       if(clear){
           llamaCppFrameWorkFFM.frameWorkInf.Context_Free(ctx.memorySegment) //消除内存泄漏测试，若需要kv缓存记忆则注释并在适当的时候清理.
       }
    }

}


fun embeddings(
    llamaCppFrameWorkFFM: LlamaCppFrameFFm,
    prompt: String,
    clear: Boolean=false,
    ctxOpt:    ()-> LlamaCtx,
    rtParmOpt: ()-> LlamaParams
): FloatArray {
    Arena.ofShared().use { arena ->

        val ctx = ctxOpt.invoke()
        val params = rtParmOpt.invoke()
        params.use_embeddings.set(1);

        val batch = LlamaBatch(arena)
        llamaCppFrameWorkFFM.frameWorkInf.InitEmbeddings(
            llamaCppFrameWorkFFM.modelSegment,
            arena.allocateUtf8String(prompt),
            batch.memorySegment,
            ctx.memorySegment
        )

        llamaCppFrameWorkFFM.frameWorkInf.SetSamplerASync(
            llamaCppFrameWorkFFM.modelSegment,
            params.memorySegment,ctx.memorySegment
        )

        val embeddings = LlamaEmb(arena)
        llamaCppFrameWorkFFM.frameWorkInf.GetEmbeddings(
            llamaCppFrameWorkFFM.modelSegment,
            batch.memorySegment,
            ctx.memorySegment,
            embeddings.memorySegment
        )

        val array = embeddings.embeddings.getForFloatArray(
            embeddings.embeddings_len.get().toLong()
        )


        llamaCppFrameWorkFFM.frameWorkInf.Batch_Free(batch.memorySegment)
        llamaCppFrameWorkFFM.frameWorkInf.Embedding_Free(embeddings.memorySegment)
        if(clear){
            llamaCppFrameWorkFFM.frameWorkInf.Context_Free(ctx.memorySegment)
        }
        return array
    }
}
