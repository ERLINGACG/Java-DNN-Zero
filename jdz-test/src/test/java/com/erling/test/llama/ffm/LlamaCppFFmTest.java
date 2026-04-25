package com.erling.test.llama.ffm;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
import com.erling.core.load.ffm.api.cpp.hook.SetConfig;
import com.erling.llama.llm.agent.LlamaAgent;
import com.erling.llama.llm.prompt.Qwen3Template;
import com.erling.test.llama.ffm.run.*;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.concurrent.atomic.AtomicInteger;

public class LlamaCppFFmTest {
    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GeneralDnnLib_Zero_llama_cpp",
            useMappingConfig = false
    )
    @SetConfig(args = {"libconfig/llama_cpp_config/qwen.json"})
    public LlamaToCppFFm llamaToCppFFm;



    public LlamaCppFFmTest(){
        SetRunTimeEnv.SET.run();
        DyLinkLibLoaderForFFM.load(this);
    }

    @Test
    public void LoadLib(){

    }

    @Test
    public void Reasoning(){

        try (Arena arena = Arena.ofConfined()) {
            LlamaCtx llamaCtx = new LlamaCtx(arena);
            llamaToCppFFm.recording(
                    new Qwen3Template().format("","你好,请介绍自己"),
                    -1, true,
                    () -> llamaCtx,
                    () -> new LlamaParams(arena),
                    (stream,token_count,tokenizer_len) -> {
                        System.out.print(stream);
                        System.out.flush();
                        return true;
                    });
        }

    }
    @Test
    public void TestAgent(){
        var agent= new LlamaAgent<LlamaCtx,LlamaParams>();
        agent.setTemplate(new Qwen3Template()).setFrameWork(this.llamaToCppFFm);
        try (Arena arena = Arena.ofConfined()) {
            AtomicInteger token_count_ = new AtomicInteger();
            AtomicInteger tokenizer_len_ = new AtomicInteger();
            agent.chat(
                    new Qwen3Template().format("","你好,请介绍自己"),true,
                    () -> new LlamaCtx(arena),
                    () -> new LlamaParams(arena),
                    (stream,token_count,tokenizer_len) -> {
                        token_count_.set(token_count);
                        tokenizer_len_.set(tokenizer_len);
                        System.out.print(stream);
                        System.out.flush();
                        return true;
                    }
            );
            System.out.println("token_count: " + token_count_.get());
            System.out.println("tokenizer_len: " + tokenizer_len_.get());
        }

    }

    @Test
    public void LoadModel(){
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment modelSegment = llamaToCppFFm.cppInf.
                    CreateLLm_GGuf_Framework(arena.allocateUtf8String("libconfig/llama_cpp_config/qwen.json"));
        }

    }

    @Test
    public void TestAutoLoad(){

    }

    @Test
    public void TestParamsStruct(){
        try (Arena arena = Arena.ofConfined()) {
            LlamaParams params = new LlamaParams(arena);
            System.out.println("top_k: " + params.top_k.get());
            System.out.println("top_p: " + params.top_p.get());
            System.out.println("temp: " + params.temp.get());
            System.out.println("n_ctx: " + params.n_ctx.get());
            System.out.println("n_batch: " + params.n_batch.get());
            System.out.println("use_gbnf: " + params.use_gbnf.get());
            System.out.println("use_embeddings: " + params.use_embeddings.get());
            System.out.println("penalty_last_n: " + params.penalty_last_n.get());
            System.out.println("penalty_repeat: " + params.penalty_repeat.get());
            System.out.println("penalty_freq: " + params.penalty_freq.get());
            System.out.println("penalty_present: " + params.penalty_present.get());
        }
    }

    @Test
    public void TestCtxStruct(){
        try (Arena arena = Arena.ofConfined()) {
            LlamaCtx ctx = new LlamaCtx(arena);

        }
    }

    @Test
    public void SetSampler(){
        try (Arena arena = Arena.ofConfined()) {
            LlamaCtx ctx = new LlamaCtx(arena);
            LlamaParams params = new LlamaParams(arena);
            MemorySegment modelSegment = llamaToCppFFm.cppInf.
                    CreateLLm_GGuf_Framework(arena.allocateUtf8String("libconfig/llama_cpp_config/qwen.json"));
            llamaToCppFFm.cppInf.SetSamplerASync(modelSegment, params.getMemorySegment(),ctx.getMemorySegment());
        }
    }

    @Test
    public void InitBatch(){
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment modelSegment = llamaToCppFFm.cppInf.
                CreateLLm_GGuf_Framework(arena.allocateUtf8String("libconfig/llama_cpp_config/qwen.json"));
            LlamaCtx ctx = new LlamaCtx(arena);
            LlamaParams params = new LlamaParams(arena);
            LlamaBatch batch = new LlamaBatch(arena);
            var startTime = System.currentTimeMillis();
            llamaToCppFFm.cppInf.SetSamplerASync(
                    modelSegment,
                    params.getMemorySegment(),
                    ctx.getMemorySegment());
            int len=llamaToCppFFm.cppInf.InitBatchASync(
                    modelSegment,
                    arena.allocateUtf8String(new Qwen3Template().format("","你好")),
                    batch.getMemorySegment(),
                    ctx.getMemorySegment()
            );

            do{
                LlamaStream stream = new LlamaStream(arena);
                llamaToCppFFm.cppInf.ReasoningASync(
                        modelSegment,
                        batch.getMemorySegment(),
                        ctx.getMemorySegment(),
                        stream.getMemorySegment()
                );
                System.out.print(stream.stream.getString());
                System.out.flush();
            }while(batch.next_token.get()!=batch.eos.get());
            var endTime = System.currentTimeMillis();
            System.out.println("time: " + (endTime - startTime));
        }
    }


    @Test
    public void TestThread() throws InterruptedException {
        MemorySegment modelSegment = null;
        try (Arena arena = Arena.ofConfined()) {
            modelSegment = llamaToCppFFm.cppInf.
                    CreateLLm_GGuf_Framework(arena.allocateUtf8String("libconfig/llama_cpp_config/qwen.json"));
        }
        MemorySegment finalModelSegment = modelSegment;
        var thread_1 = new Thread(() -> {
             try (Arena arena = Arena.ofConfined()) {
                 LlamaCtx ctx = new LlamaCtx(arena);
                 LlamaParams params = new LlamaParams(arena);
                 LlamaBatch batch = new LlamaBatch(arena);
                 llamaToCppFFm.cppInf.SetSamplerASync(
                         finalModelSegment,
                        params.getMemorySegment(),
                        ctx.getMemorySegment());
                 llamaToCppFFm.cppInf.InitBatchASync(
                         finalModelSegment,
                        arena.allocateUtf8String(new Qwen3Template().format("","你好")),
                        batch.getMemorySegment(),
                        ctx.getMemorySegment()
                );
                 do{
                    LlamaStream stream = new LlamaStream(arena);
                    llamaToCppFFm.cppInf.ReasoningASync(
                            finalModelSegment,
                            batch.getMemorySegment(),
                            ctx.getMemorySegment(),
                            stream.getMemorySegment()
                    );
                    System.out.println("stream_1: " + stream.stream.getString());
//                    System.out.print(stream.stream.getString());
//                    System.out.flush();
                }while(batch.next_token.get()!=batch.eos.get());
             }
        });

        var thread_2 = new Thread(() -> {
            try (Arena arena = Arena.ofConfined()) {
                LlamaCtx ctx = new LlamaCtx(arena);
                LlamaParams params = new LlamaParams(arena);
                LlamaBatch batch = new LlamaBatch(arena);
                llamaToCppFFm.cppInf.SetSamplerASync(
                        finalModelSegment,
                        params.getMemorySegment(),
                        ctx.getMemorySegment());
                llamaToCppFFm.cppInf.InitBatchASync(
                        finalModelSegment,
                        arena.allocateUtf8String(new Qwen3Template().format("","请介绍你自己")),
                        batch.getMemorySegment(),
                        ctx.getMemorySegment()
                );
                do{
                    LlamaStream stream = new LlamaStream(arena);
                    llamaToCppFFm.cppInf.ReasoningASync(
                            finalModelSegment,
                            batch.getMemorySegment(),
                            ctx.getMemorySegment(),
                            stream.getMemorySegment()
                    );
                    System.out.println("stream_2: " + stream.stream.getString());
//                    System.out.print(stream.stream.getString());
//                    System.out.flush();
                }while(batch.next_token.get()!=batch.eos.get());
            }
        });
        thread_1.start();
        thread_2.start();
        thread_1.join();
        thread_2.join();
    }
}
