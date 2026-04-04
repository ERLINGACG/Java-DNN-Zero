package com.erling.jdz.llm.framework.gguf;

import com.erling.jdz.llm.ContextInit;
import com.erling.jdz.llm.LLmChat;
import com.erling.jdz.llm.RunTimeInit;
import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Batch;
import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Context;
import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Context_RTParam;
import com.erling.jdz.llm.framework.gguf.stream.LLM_GGUF_Stream;
import com.erling.jdz.llm.frameworkinf.gguf.LLmGGufFrameWorkInf;
import com.erling.jdz.load.ann.init;
import com.sun.jna.Pointer;

import java.nio.charset.StandardCharsets;

public class LLmGGufFrameWork implements LLmChat {

    public   LLmGGufFrameWorkInf frameWork;

    private Pointer frameworkPtr;

    public final String configPath;


     public LLmGGufFrameWork(String configPath){
        this.configPath=configPath;
     }

     @init
     public void init_model(){
        this.frameworkPtr = frameWork.CreateLLm_GGuf_Framework(this.configPath);
     }

      public void destroy(){
        frameWork.DestroyLLm_GGuf_Framework(frameworkPtr);
     }


      public LLmGGufFrameWork setSampler(int top_k, float top_p, float temp){
        frameWork.SetSamplerRT(
                this.frameworkPtr,
                "".getBytes(StandardCharsets.UTF_8), false,
                top_k, top_p, temp
        );
        return this;
     }

     public LLmGGufFrameWork setSampler(String gbnf,boolean use_grammar, int top_k, float top_p, float temp){
         // 修复：添加UTF-8空终止符，避免底层读取内存乱码
         byte[] gbnfBytes = new byte[gbnf.getBytes(StandardCharsets.UTF_8).length + 1];
         System.arraycopy(
                 gbnf.getBytes(StandardCharsets.UTF_8),
                 0, gbnfBytes, 0,
                 gbnf.getBytes(StandardCharsets.UTF_8).length
         );
         gbnfBytes[gbnfBytes.length - 1] = 0; // 末尾加\0终止符
        frameWork.SetSamplerRT(
                this.frameworkPtr,gbnfBytes,
                use_grammar,
                top_k,top_p,temp
        );
        return this;
     }

     public void initBatch(LLM_GGUF_Batch batch,String prompt){
        frameWork.InitBatch(this.frameworkPtr,prompt.getBytes(StandardCharsets.UTF_8),batch);
     }

     public void reasoning(LLM_GGUF_Batch batch, LLM_GGUF_Stream stream){
        frameWork.Reasoning(this.frameworkPtr,batch,stream);
     }

     public void batchFree(LLM_GGUF_Batch batch){
        frameWork.Batch_Free(batch);
     }

     public void setSamplerAsync(LLM_GGUF_Context_RTParam param, LLM_GGUF_Context context){
        frameWork.SetSamplerASync(this.frameworkPtr,param,context);
     }

     public void initBatchAsync(LLM_GGUF_Batch batch,String prompt, LLM_GGUF_Context context){
        frameWork.InitBatchASync(this.frameworkPtr,prompt.getBytes(StandardCharsets.UTF_8),batch,context);
     }

     public void reasoningAsync(LLM_GGUF_Batch batch, LLM_GGUF_Context context, LLM_GGUF_Stream stream){
        frameWork.ReasoningASync(this.frameworkPtr,batch,context,stream);
     }

     public void initEmbeddings(LLM_GGUF_Batch batch,String prompt, LLM_GGUF_Context context){
        frameWork.InitEmbeddings(this.frameworkPtr,prompt.getBytes(StandardCharsets.UTF_8),batch,context);
     }

     public void getEmbeddings(LLM_GGUF_Batch batch, LLM_GGUF_Context context){
        frameWork.GetEmbeddings(this.frameworkPtr,batch,context);
     }



     public String iChat(LLM_GGUF_Context_RTParam param, LLM_GGUF_Context ctx, String prompt, int maxTokens){
         var batch = new LLM_GGUF_Batch();
         StringBuilder result = new StringBuilder();
         setSamplerAsync(param,ctx);
         initBatchAsync(batch,prompt,ctx);
//         System.out.println("eos:"+batch.eos);
         int token_count = 0;
         do {
             var stream = new LLM_GGUF_Stream();
             reasoningAsync(batch, ctx, stream);
             System.out.print(stream.getStream());
             System.out.flush();
             result.append(stream.getStream());
             token_count++;

         } while (batch.next_token != batch.eos && token_count < maxTokens);

         batchFree(batch);
         return result.toString();
     }

     public String iChatSetEOS(
             LLM_GGUF_Context_RTParam param,
             LLM_GGUF_Context ctx,
             String prompt,
             int maxTokens, int eos
     ){
         var batch = new LLM_GGUF_Batch();
         StringBuilder result = new StringBuilder();
         setSamplerAsync(param,ctx);
         initBatchAsync(batch,prompt,ctx);
         batch.eos= eos;
         do {
             var stream = new LLM_GGUF_Stream();
             reasoningAsync(batch, ctx, stream);
             System.out.print(stream.getStream());
             System.out.flush();
             result.append(stream.getStream());

         } while (batch.next_token != batch.eos);

         batchFree(batch);
         return result.toString();
     }

     @Override
     public String chat(String prompt, int maxTokens, ContextInit<?> context, RunTimeInit<?> runTimeInit) {

        var param =(LLM_GGUF_Context_RTParam) runTimeInit.run();
        var ctx = (LLM_GGUF_Context) context.run();

         return iChat(param, ctx, prompt, maxTokens);
     }

     public String chat(String prompt, int maxTokens, ContextInit<?> context, RunTimeInit<?> runTimeInit, int eos) {
        var param =(LLM_GGUF_Context_RTParam) runTimeInit.run();
        var ctx = (LLM_GGUF_Context) context.run();
        return iChatSetEOS(param, ctx, prompt, maxTokens, eos);
     }



    @Override
    public String charExample(String prompt, int maxTokens, ContextInit<?> context, RunTimeInit<?> runTimeInit, String gbnf) {
         var param =(LLM_GGUF_Context_RTParam) runTimeInit.run();
         param.use_gbnf=true;
         param.gbnf_str=gbnf;
//         System.out.println(param.gbnf_str);
         var ctx = (LLM_GGUF_Context) context.run();
//         param.gbnf_str= """
//                            root ::= "answer:" "{" name "," age "}"
//                            name ::= " 'name:'" [^,]+
//                            age  ::= " 'age:' " [0-9]+
//                            """;
         return iChat(param, ctx, prompt, maxTokens);
    }


    public void embeddings(String prompt,RunTimeInit<?> runTimeInit,ContextInit<?> context){
        var param =(LLM_GGUF_Context_RTParam) runTimeInit.run();
        var ctx = (LLM_GGUF_Context) context.run();
        var batch = new LLM_GGUF_Batch();
        setSamplerAsync(param,ctx);
        initEmbeddings(batch,prompt,ctx);
        getEmbeddings(batch,ctx);
    }

//    @Override
//    public LLM_GGUF_Context setContext(LLM_GGUF_Context context) {
//        return context;
//    }
//     public void json_ExampleSend(String json){
//        frameWork.Json_ExampleSend(json);
//     }


}
