package com.erling.jdz.llm.framework.gguf;

import com.erling.jdz.llm.frameworkinf.gguf.LLmGGufFrameWorkInf;
import com.erling.jdz.load.ann.init;
import com.sun.jna.Pointer;

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

      public void exampleSend(){
        frameWork.ExampleSend(frameworkPtr);
     }
}
