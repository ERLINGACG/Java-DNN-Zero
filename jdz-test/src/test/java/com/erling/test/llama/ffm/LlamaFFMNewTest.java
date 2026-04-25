package com.erling.test.llama.ffm;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
import com.erling.core.load.ffm.api.cpp.hook.SetConfig;
import com.erling.llama.llm.agent.LlamaAgent;
import com.erling.llama.llm.framework.ffm.LlamaCppFrameFFm;
import com.erling.llama.llm.framework.ffm.struct.LlamaCtx;
import com.erling.llama.llm.framework.ffm.struct.LlamaParams;
import com.erling.llama.llm.prompt.Qwen3Template;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;
import java.util.concurrent.atomic.AtomicInteger;

public class LlamaFFMNewTest {
//    @JdzFrameFFM(
//            rootPath = "./libconfig/share",
//            name = "GeneralDnnLib_Zero_llama_cpp",
//            useMappingConfig = false
//    )
//    @SetConfig(
//            args = {"libconfig/llama_cpp_config/qwen.json"}
//    )
//    public LlamaToCppFFm llamaToCppFFm;

    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GeneralDnnLib_Zero_llama_cpp",
            useMappingConfig = false
    )
    @SetConfig(
            args = {"libconfig/llama_cpp_config/qwen.json"}
    )
    public LlamaCppFrameFFm NewllamaCppFrameFFm;


    public LlamaFFMNewTest(){
        SetRunTimeEnv.SET.run();
        DyLinkLibLoaderForFFM.load(this);
    }

    @Test
    public void load(){

    }

    @Test
    public void chat(){
        var agent = new LlamaAgent<LlamaCtx,LlamaParams>().
                setFrameWork(NewllamaCppFrameFFm).
                setTemplate(new Qwen3Template());
        var start = System.currentTimeMillis();
        AtomicInteger count = new AtomicInteger();
        try(Arena arena = Arena.ofConfined()){
            agent.chat("你好,请介绍你自己",true,
                    ()->new LlamaCtx(arena),
                    ()->{
                        var params = new LlamaParams(arena);
                        params.n_batch.set(512);
                        params.n_ctx.  set(2048);
                        params.temp.   set(0.7f);
                        return params;
                    },
                    (stream,token_count,tokenizer_len)->{
                        count.set(token_count);
                        System.out.print(stream);
                        System.out.flush();
                        return true;
                    }
            );
        }
        System.out.println("Send "+count.get()+" tokens");
        System.out.println("cost time: "+(System.currentTimeMillis()-start));
    }

    @Test
    public void embeddings(){
//        for(int i=0; i<2; i++){
//            var embeddings = NewllamaCppFrameFFm.test();
//        }
        try(Arena arena = Arena.ofConfined()){
            var embeddings = NewllamaCppFrameFFm.embeddings("你好,请介绍你自己",true,
                    ()->new LlamaCtx(arena),
                    ()->new LlamaParams(arena)
            );
            System.out.println(embeddings.length);
        }
    }
    @Test
    public void embeddings_thread(){
//        for(int i=0; i<2; i++){
//            var embeddings = NewllamaCppFrameFFm.test();
//        }
        var thread_1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try(Arena arena = Arena.ofConfined()){
                    var embeddings = NewllamaCppFrameFFm.embeddings("你好,请介绍你自己",true,
                            ()->new LlamaCtx(arena),
                            ()->new LlamaParams(arena)
                    );
                    System.out.println(embeddings.length);
                }
            }
        });
        var thread_2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try(Arena arena = Arena.ofConfined()){
                    var embeddings = NewllamaCppFrameFFm.embeddings("你好,请介绍你自己",true,
                            ()->new LlamaCtx(arena),
                            ()->new LlamaParams(arena)
                    );
                    System.out.println(embeddings.length);
                }
            }
        });

        thread_1.start();
        thread_2.start();
        try {
            thread_1.join();
            thread_2.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
