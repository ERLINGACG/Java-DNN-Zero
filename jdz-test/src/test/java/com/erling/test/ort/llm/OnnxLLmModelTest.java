package com.erling.test.ort.llm;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
import com.erling.core.load.ffm.api.cpp.hook.SetConfig;
import com.erling.ort.llm.framework.ffm.OnnxRtLlmFramework;
import com.erling.ort.llm.model.OnnxLLmModel;
import com.erling.ort.llm.struct.OnnxRtLlmCtx;
import com.erling.ort.llm.struct.OnnxRtLlmKv;
import com.erling.ort.llm.struct.OnnxRtLlmParam;
import com.erling.tokens.framework.TokensFramework;
import com.erling.tokens.struct.TokenizerIDS;
import com.erling.tokens.struct.TokenizerLoader;
import com.erling.tokens.struct.TokenizerTokens;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class OnnxLLmModelTest {


    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GeneralDnnLib_Zero_ort_llm",
            useMappingConfig = false
    )
    @SetConfig(
            args = {
                    "./libconfig/onnx/glm.json"
            }
    )
    public OnnxRtLlmFramework onnxRtLlmFramework;

    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GDLZTokens",
            useMappingConfig = false
    )
    @SetConfig(
            args = {
                    "E:\\ZeroPlan\\Java\\JDnn-Zero\\JDnnZero\\jdz-test\\libconfig\\onnx\\glm_tokenzer.json"
            }
    )
    public TokensFramework tokensFramework;

    public OnnxLLmModel model;

    public Arena lifeArena=Arena.ofShared();
    public TokenizerLoader tokenizerLoader;

    public OnnxLLmModelTest(){
        SetRunTimeEnv.SET.run();
        DyLinkLibLoaderForFFM.load(this);

        this.tokenizerLoader=new TokenizerLoader(lifeArena);
        tokenizerLoader.setIFramework(tokensFramework);
        this.model=new OnnxLLmModel().
                setOnnxRtLlmFramework(onnxRtLlmFramework).
                setTokenizerLoader(tokenizerLoader);
    }

    @Test
    public void Autoload(){

    }

    @Test
    public void run(){
         try(Arena arena=Arena.ofShared()){
             var ctx=new OnnxRtLlmCtx(arena);
             var kv=new OnnxRtLlmKv(arena);
             var param=new OnnxRtLlmParam(arena);
             param.top_k.set(30);
             param.top_p.set(0.9f);
             param.temperature.set(0.7f);
             var token=new TokenizerTokens(arena);

             model.setSample(param,ctx);
             model.prefillFor1DRoPE(arena,ctx,kv,
                     tokenizerLoader.encodeForLong(new TokenizerIDS(arena), """
                             [gMASK]<sop><|system|>你是一个有用的AI助手。
                             <|user|>你好
                             <|assistant|>
                             """)
             );
             System.out.print(tokenizerLoader.decode(token, Math.toIntExact(ctx.next_token_id.get())));
             System.out.flush();
             while (ctx.next_token_id.get()!=59253){ //151643为deepseek的结束符，59253为glm的结束符
                 model.decodeFor1DRoPE(ctx,kv);
                 System.out.print(tokenizerLoader.decode(token, Math.toIntExact(ctx.next_token_id.get())));
                 System.out.flush();
             }

             System.out.println("\n");
             model.prefillFor1DRoPE(arena,ctx,kv,
                     tokenizerLoader.encodeForLong(new TokenizerIDS(arena), """
                             <｜User｜>
                             请你写一段简单的python代码，比如打印hello world
                             <｜Assistant｜>
                             """)
             );
             System.out.print(tokenizerLoader.decode(token, Math.toIntExact(ctx.next_token_id.get())));
             System.out.flush();
             while (ctx.next_token_id.get()!=59253){ //59253为glm的结束符
                 model.decodeFor1DRoPE(ctx,kv);
                 System.out.print(tokenizerLoader.decode(token, Math.toIntExact(ctx.next_token_id.get())));
                 System.out.flush();
             }
//
             System.out.println("\n");
             model.prefillFor1DRoPE(arena,ctx,kv,
                     tokenizerLoader.encodeForLong(new TokenizerIDS(arena), """
                             <｜User｜>
                             你能总结一下我们的对话历史吗
                             <｜Assistant｜>
                             """)
             );
             System.out.print(tokenizerLoader.decode(token, Math.toIntExact(ctx.next_token_id.get())));
             System.out.flush();
             while (ctx.next_token_id.get()!=59253){ //59253为glm的结束符
                 model.decodeFor1DRoPE(ctx,kv);
                 System.out.print(tokenizerLoader.decode(token, Math.toIntExact(ctx.next_token_id.get())));
                 System.out.flush();
             }
         }
    }

    @Test
    public void run2(){
        try(Arena arena=Arena.ofShared()){
           var ctx=new OnnxRtLlmCtx(arena);
           var kv=new OnnxRtLlmKv(arena);
           Map<Integer,Long> times=new HashMap<>();
           AtomicLong start_time = new AtomicLong(System.currentTimeMillis());
           model.generate("""
                              [gMASK]<sop><|system|>你是一个有用的AI助手。
                             <|user|>你好啊，介绍自己一下吧
                             <|assistant|>
                           """,59253,
                   () -> new OnnxRtLlmParam(arena),
                   () -> ctx,
                   () -> kv,
                   (token_str,count) -> {
                       System.out.print(token_str);
                       times.put(count, System.currentTimeMillis()- start_time.get());
                       start_time.set(System.currentTimeMillis());
                       System.out.flush();
                       return true;
                   }
            );
           System.out.println("\n");
           System.out.println(times);
           times.clear();
           model.generate("""
                              [gMASK]<sop><|system|>你是一个有用的AI助手。
                             <|user|>你能写一段简单的C++代码吗，比如打印hello world吗
                             <|assistant|>
                           """,59253,
                    () -> new OnnxRtLlmParam(arena),
                    () -> ctx,
                    () -> kv,
                    (token_str,count) -> {
                        System.out.print(token_str);
                        times.put(count, System.currentTimeMillis()- start_time.get());
                        start_time.set(System.currentTimeMillis());
                        System.out.flush();
                        return true;
                    }
            );
            System.out.println("\n");
            System.out.println(times);
            times.clear();
            model.generate("""
                              [gMASK]<sop><|system|>你是一个有用的AI助手。
                             <|user|>写一段python代码，请注意是python,你能用python写一个简单的hello world吗
                             <|assistant|>
                           """,59253,
                    () -> new OnnxRtLlmParam(arena),
                    () -> ctx,
                    () -> kv,
                    (token_str,count) -> {
                        System.out.print(token_str);
                        times.put(count, System.currentTimeMillis()- start_time.get());
                        start_time.set(System.currentTimeMillis());
                        System.out.flush();
                        return true;
                    }
            );
            System.out.println("\n");
            System.out.println(times);
            times.clear();

            System.out.println("\n");
            model.generate("""
                              [gMASK]<sop><|system|>你是一个有用的AI助手。
                             <|user|>我们此前聊了什么编程语言
                             <|assistant|>
                           """,59253,
                    () -> new OnnxRtLlmParam(arena),
                    () -> ctx,
                    () -> kv,
                    (token_str,count) -> {
                        System.out.print(token_str);
                        times.put(count, System.currentTimeMillis()- start_time.get());
                        start_time.set(System.currentTimeMillis());
                        System.out.flush();
                        return true;
                    }
            );
            System.out.println("\n");
            System.out.println(times);
            times.clear();
        }
    }
}
