package com.erling.jdz.llm.framework.gguf;

import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Batch;
import com.erling.jdz.llm.framework.gguf.stream.LLM_GGUF_Stream;
import com.erling.jdz.llm.frameworkinf.gguf.LLmGGufFrameWorkInf;
import com.erling.jdz.load.ann.init;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.nio.charset.StandardCharsets;

public class LLmGGufFrameWork {

    private  LLmGGufFrameWorkInf frameWork;

    private Pointer frameworkPtr;

    private final String configPath;


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

     @Deprecated(since = "该方法为测试方法")
      public void exampleSend(){
        frameWork.ExampleSend(frameworkPtr);
     }

     @Deprecated(since = "该方法为测试方法")
     public void exampleSend(byte[] prompt,LLM_GGUF_Batch batch, LLM_GGUF_Stream stream){
         System.out.println("java batch");
         int res = frameWork.SendExample(frameworkPtr,prompt,batch,stream);
         System.out.println("res:"+res);
         System.out.println(stream.str_len);
         System.out.println(stream.getStream());
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
}
