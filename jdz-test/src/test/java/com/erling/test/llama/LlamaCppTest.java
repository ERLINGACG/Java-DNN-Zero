package com.erling.test.llama;

import com.erling.core.load.ann.JdzFramework;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.jna.DyLinkLibLoader;
import com.erling.llama.llm.agent.LlamaAgent;
import com.erling.llama.llm.framework.jna.LlamaCppFrameWorkJNA;
import com.erling.llama.llm.framework.jna.LlamaCppInf;
import com.erling.llama.llm.prompt.Qwen3Template;
import com.erling.llama.llm.framework.jna.struct.LLM_GGUF_Context;
import com.erling.llama.llm.framework.jna.struct.LLM_GGUF_Context_RTParam;
import org.junit.jupiter.api.Test;

public class LlamaCppTest {

    @JdzFramework(
            rootPath = "E:/ZeroPlan/Java/JDnn-Zero/JDnnZero/jdz-test/libconfig/share/",
            name = "GeneralDnnLib_Zero_llama_cpp",
            mapping = LlamaCppInf.class,
            isConfig = true,
            configPath = "libconfig/llama_cpp_config/qwen.json"

    )
    LlamaCppFrameWorkJNA llamaCppFrameWorkJNA;


    public LlamaCppTest(){
        SetRunTimeEnv.SET.run();
        DyLinkLibLoader.Load(this);
    }

    @Test
    public void test_init_model(){

    }

//    @Test
//    public void test_recording(){
//        var template = new Qwen3Template();
//        var prompt = template.format("你是一个人工智能机器人","你好,请介绍你自己");
////        System.out.println(prompt);
//        llamaCppFrameWorkJNA.recording(
//                prompt,
//                LLM_GGUF_Context_RTParam::new,
//                LLM_GGUF_Context::new,
//                (stream, count, tokenizer) -> {
//                    System.out.print(stream);
//                    System.out.flush();
//                    return true;
//                }
//        );
//    }

    @Test
    public void test_agent(){
        var startTime = System.currentTimeMillis();
       var agent= new LlamaAgent<LLM_GGUF_Context_RTParam,LLM_GGUF_Context>().
               setFrameWork(this.llamaCppFrameWorkJNA).
               setTemplate(new Qwen3Template()).
               chat("你好,请介绍你自己",true,
                       LLM_GGUF_Context_RTParam::new,
                       LLM_GGUF_Context::new,
                       (stream, count, tokenizer) -> {
                            System.out.print(stream);
                            System.out.flush();
                            return count <= 1024;
                       }
               );
        var endTime = System.currentTimeMillis();
        System.out.println("time: " + (endTime - startTime));
    }

//    @Test
//    public void test_agent_use_tools(){
//        var agent= new LlamaAgent().
//                setFrameWork(this.llamaCppFrameWorkJNA).
//                setTemplate(new Qwen3Template()).setEngine(
//                        new FunctionEngine(),
//                        ()->{
//                            class Tool{
//                                @FuncTools(
//                                        description = "获取当前工作目录"
//                                )
//                                public String getWorkPath(){
//                                    return System.getProperty("user.dir");
//                                }
//
//                                @FuncTools(
//                                        description = "检查当前工作目录下的文件"
//                                )
//                                public String checkWorkPathItem() throws IOException, InterruptedException {
//                                    ProcessBuilder pb = new ProcessBuilder("powerShell", "-c", "ls");
//
//                                    Process process = pb.start();
//
//                                    // 读取标准输出流
//                                    BufferedReader reader = new BufferedReader(
//                                            new InputStreamReader(process.getInputStream(), "GBK")
//                                    );
//                                    StringBuilder line_buffer = new StringBuilder();
//                                    String line;
//                                    while ((line = reader.readLine()) != null) {
//                                        line_buffer.append(line);
//                                        line_buffer.append("\n");
//                                    }
////                                    System.out.println(line_buffer);
//                                    process.waitFor();
//                                    return line_buffer.toString();
//                                }
//
//                                @FuncTools(
//                                        description = "创建文件测试文件"
//                                )
//                                public boolean createTestFileTxt(@FuncParam(description = "文件名")String fileName) throws IOException {
//                                    try {
//                                        // 获取当前工作目录（假设已有 getWorkPath 工具返回的路径）
//                                        System.out.println("创建文件名：" + fileName);
//                                        if (fileName.startsWith("\"") && fileName.endsWith("\"")) {
//                                            fileName = fileName.substring(1, fileName.length() - 1);
//                                        }
//                                        String workPath = getWorkPath(); // 或者通过其他方式传入
//                                        Path filePath = Paths.get(workPath, fileName);
//                                        // 确保父目录存在
//                                        Files.createDirectories(filePath.getParent());
//                                        // 创建文件（如果已存在则先删除或覆盖）
//                                        Files.createFile(filePath);
//                                        System.out.println("文件已创建于：" + filePath.toAbsolutePath());
//                                        return true;
//                                    } catch (IOException e) {
//                                        System.err.println("创建文件失败：" + e.getMessage());
//                                        return false;
//                                    }
//
//                                }
//
//
//                            }
//                            return new Tool();
//                        }
//                ).
//                setDebug(true);
//        agent.useTools(()->new UserToolRT(
//                "你是一个人工智能机器人",
//                "创建一个test.txt文件",
//                ()->{
//                    var rtParam = new LLM_GGUF_Context_RTParam();
//                    rtParam.n_ctx=4096;
//                    rtParam.n_batch=4096;
//                    rtParam.temp=0.5f;
//                    return rtParam;
//                },
//                LLM_GGUF_Context::new,
//                (stream, count, tokenizer) -> {
//                    System.out.print(stream);
//                    System.out.flush();
//                    return true;
//                }
//        ));
//    }


}
