package com.erling.test.llama;

import com.erling.core.load.ann.JdzFramework;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.jna.DyLinkLibLoader;
import com.erling.llama.llm.agent.LlamaAgent;
import com.erling.llama.llm.framework.LlamaCppFrameWork;
import com.erling.llama.llm.framework.LlamaCppInf;
import com.erling.llama.llm.function.FunctionEngine;
import com.erling.llama.llm.function.ann.FuncTools;
import com.erling.llama.llm.function.kt.UserToolRT;
import com.erling.llama.llm.prompt.Qwen3Template;
import com.erling.llama.llm.struct.LLM_GGUF_Context;
import com.erling.llama.llm.struct.LLM_GGUF_Context_RTParam;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LlamaCppTest {

    @JdzFramework(
            rootPath = "E:/ZeroPlan/Java/JDnn-Zero/JDnnZero/jdz-test/libconfig/share/",
            name = "GeneralDnnLib_Zero_llama_cpp",
            mapping = LlamaCppInf.class,
            isConfig = true,
            configPath = "libconfig/llama_cpp_config/qwen.json"

    )
    LlamaCppFrameWork llamaCppFrameWork;


    public LlamaCppTest(){
        SetRunTimeEnv.SET.run();
        DyLinkLibLoader.Load(this);
    }

    @Test
    public void test_init_model(){

    }

    @Test
    public void test_recording(){
        var template = new Qwen3Template();
        var prompt = template.format("你是一个人工智能机器人","你好,请介绍你自己");
//        System.out.println(prompt);
        llamaCppFrameWork.recording(
                prompt,
                LLM_GGUF_Context_RTParam::new,
                LLM_GGUF_Context::new,
                (stream, count) -> {
                    System.out.print(stream);
                    System.out.flush();
                    return true;
                }
        );
    }

    @Test
    public void test_agent(){
       var agent= new LlamaAgent().
               setFrameWork(this.llamaCppFrameWork).
               setTemplate(new Qwen3Template()).
               chat("你好,请介绍你自己",
                       LLM_GGUF_Context_RTParam::new,
                       LLM_GGUF_Context::new,
                       (stream, count) -> {
                            System.out.print(stream);
                            System.out.flush();
                            return count <= 1024;
                       }
               );
    }

    @Test
    public void test_agent_use_tools(){
        var agent= new LlamaAgent().
                setFrameWork(this.llamaCppFrameWork).
                setTemplate(new Qwen3Template()).setEngine(
                        new FunctionEngine(),
                        ()->{
                            class Tool{
                                @FuncTools(
                                        description = "获取当前工作目录"
                                )
                                public String getWorkPath(){
                                    return System.getProperty("user.dir");
                                }

                                @FuncTools(
                                        description = "检查当前工作目录下的文件"
                                )
                                public String checkWorkPathItem() throws IOException, InterruptedException {
                                    ProcessBuilder pb = new ProcessBuilder("powerShell", "-c", "ls");

                                    Process process = pb.start();

                                    // 读取标准输出流
                                    BufferedReader reader = new BufferedReader(
                                            new InputStreamReader(process.getInputStream(), "GBK")
                                    );
                                    StringBuilder line_buffer = new StringBuilder();
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        line_buffer.append(line);
                                        line_buffer.append("\n");
                                    }
//                                    System.out.println(line_buffer);
                                    process.waitFor();
                                    return line_buffer.toString();
                                }
                            }
                            return new Tool();
                        }
                ).
                setDebug(true);
        agent.useTools(()->new UserToolRT(
                "你是一个人工智能机器人",
                "查看当前工作目录，并检查当前工作目录下的文件",
                ()->{
                    var rtParam = new LLM_GGUF_Context_RTParam();
                    rtParam.n_ctx=4096;
                    rtParam.n_batch=4096;
                    rtParam.temp=0.5f;
                    return rtParam;
                },
                LLM_GGUF_Context::new,
                (stream, count) -> {
                    System.out.print(stream);
                    System.out.flush();
                    return true;
                }
        ));
    }
}
