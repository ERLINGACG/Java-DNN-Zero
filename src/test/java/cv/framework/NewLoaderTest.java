package cv.framework;

import com.erling.jdz.cv.framework.yolo.YoloFrameWork;
import com.erling.jdz.cv.framework.yolo.YoloOutput;
import com.erling.jdz.cv.frameworkinf.yolo.YoloFrameWorkInf;
import com.erling.jdz.llm.framework.gguf.LLmGGufFrameWork;
import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Batch;
import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Context;
import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Context_RTParam;
import com.erling.jdz.llm.framework.gguf.stream.LLM_GGUF_Stream;
import com.erling.jdz.llm.frameworkinf.gguf.LLmGGufFrameWorkInf;
import com.erling.jdz.load.DyLinkLibLoader;
import com.erling.jdz.load.RunEnv;
import com.erling.jdz.load.ann.ConfigPath;
import com.erling.jdz.load.ann.DyLinkLib;
import com.erling.jdz.load.ann.Mapping;
import com.erling.jdz.uitls.run.TaskRun;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class NewLoaderTest {


    @DyLinkLib(path = "clibconf/lib/GeneralDnnLib_Framework_Lib.dll")
    @ConfigPath("clibconf/modelconf/yolo_config.json")
    @Mapping(YoloFrameWorkInf.class)
    YoloFrameWork yoloFrameWork;

    @DyLinkLib(path = "E:\\ZeroPlan\\C++\\GeneralDnnLib-Zero\\cmake-build-release-visual-studio\\gdlz_test_lib\\Release\\GeneralDnnLib_Framework_Lib.dll")
    @ConfigPath("clibconf/modelconf/llm_gguf_config.json")
    @Mapping(LLmGGufFrameWorkInf.class)
    public LLmGGufFrameWork llmGGufFrameWork;

    public NewLoaderTest() throws IllegalAccessException {
        RunEnv.SET_ENV.run();
        DyLinkLibLoader.load(this);

    }

    @Test
    public void init_test(){

    }

    @Test
    void testYoloFrameWork() throws IllegalAccessException, IOException {
        byte[] imageBytes = Files.readAllBytes(Path.of("./image/bus.jpg"));
        YoloOutput output = new YoloOutput();
        yoloFrameWork.detect(imageBytes, output);
        yoloFrameWork.destroy();
    }

    @Test
    public void testBatch() {
        var batch = new LLM_GGUF_Batch();
        long startTime = System.currentTimeMillis();
        String prompt = "请你输出json格式的文本{ name:\"张三\", age:30 }";

// 直接用 + 拼接，完全还原C++注释里的格式规则
        String finalPrompt = "<|im_start|>user\n"
                + "请严格按照以下要求回答我的问题：\n"
                + "1. 输出内容**仅包含标准JSON字符串**，不要有任何多余的文字、解释、备注；\n"
                + "2. JSON结构必须包含以下字段：name（商品名称）、price（价格）、attributes（属性，包含color、storage）；\n"
                + "3. 示例输出格式：\n"
                + "{\"name\":\"商品名称\",\"price\":0,\"attributes\":{\"color\":\"颜色\",\"storage\":\"存储容量\"}}\n"
                + "4. 基于我的问题生成JSON：" + "" + "\n"
                + "<|im_end|>\n"
                + "<|im_start|>assistant\n";
        llmGGufFrameWork.initBatch(batch,finalPrompt);
//        StringBuilder res= new StringBuilder();
        int tokenCount = 0;
        do {
            var stream = new LLM_GGUF_Stream();
            llmGGufFrameWork.reasoning(batch, stream);
            System.out.print(stream.getStream());
            tokenCount++;
//            if(tokenCount>=20) break;

//            res.append(stream.getStream());
        } while (batch.next_token != batch.eos);

        System.out.println("tokenCount:"+tokenCount);
        llmGGufFrameWork.batchFree(batch);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println();
        System.out.println("推理耗时"+duration+"ms");
    }


    @Test
    @DisplayName("测试LLM GGUF 推理")
    public void testLLmGGufFrameWork() {
        var batch = new LLM_GGUF_Batch();
        String prompt= """
                请严格按照以下要求回答我的问题：
                                        1. 输出内容**仅包含标准JSON字符串**，不要有任何多余的文字、解释、备注；
                                        2. JSON结构必须包含以下字段：name（商品名称）、price（价格）、attributes（属性，包含color、storage）；
                                        3. 示例输出格式：
                                        {"name":"商品名称","price":0,"attributes":{"color":"颜色","storage":"存储容量"}}
                                        4. 基于我的问题生成JSON：""";
        String prompt_py= """
                 请写一段如何调用tensorRT的python代码
                """;
        String prompt1= """
              有一个人李四，20岁，
              请按格式输出他们两个的信息
                """;

        String finalPrompt =
                        """
                        <|im_start|>user
                        """ + prompt1 + """
                        <|im_end|>
                        <|im_start|>assistant
                        """;

        llmGGufFrameWork.setSampler("""
                root ::= "answer:" "{" name "," age "}"
                name ::= " 'name:'" [^,]+
                age  ::= " 'age:' " [0-9]+""",
                true,
                30,0.9f,0.1f
        ).initBatch(batch,finalPrompt);
//        llmGGufFrameWork.setSampler(30,0.9f,0.1f);
//        llmGGufFrameWork.initBatch(batch,finalPrompt);
        do {

            var stream = new LLM_GGUF_Stream();
            llmGGufFrameWork.reasoning(batch, stream);

            System.out.print(stream.getStream());

        } while (batch.next_token != batch.eos);
        llmGGufFrameWork.batchFree(batch);
        llmGGufFrameWork.destroy();

    }

    public void test1() {

    }


    @Test
    @DisplayName("function call 测试")
    public void testFunctionCall() {

        String prompt1= """
              
                查询李四的信息
                """;

        String finalPrompt =
                        """
                        <|im_start|>user
                        """ + prompt1 + """
                        <|im_end|>
                        <|im_start|>assistant
                        """;
        String gbnf= """
                 root ::= "answer:" "{" name "," age "}"
                                name ::= " 'name:'" [^,]+
                                age  ::= " 'age:' " [0-9]+
                 """;



        var batch = new LLM_GGUF_Batch();
        llmGGufFrameWork.setSampler(
                """
                     root ::= "'answer:'{'functionName:' " functionItem "," " 'arg:'[" arg "]}"
                     functionItem ::= "get_Weather" | "get_StockPrice" | "sub" | "get_User"
                     arg ::= [^\\]]+
                     """, true,
                30,0.9f,0.1f
        )
                .initBatch(batch,finalPrompt);

        do {

            var stream = new LLM_GGUF_Stream();
            llmGGufFrameWork.reasoning(batch, stream);

            System.out.print(stream.getStream());

        } while (batch.next_token != batch.eos);
        llmGGufFrameWork.batchFree(batch);
        llmGGufFrameWork.destroy();

    }
    @Test
    @DisplayName("测试LLM GGUF 推理异步")
    public void testAsync() throws IllegalAccessException, InterruptedException {



        var thread1=new Thread(()->{
            String prompt1= """
          
            有个人叫lisi，20岁
            """;

            String finalPrompt =
                    """
                    <|im_start|>user
                    """ + prompt1 + """
                    <|im_end|>
                    <|im_start|>assistant
                    """;
            var batch = new LLM_GGUF_Batch();
            var context = new LLM_GGUF_Context();
            var param = new LLM_GGUF_Context_RTParam();
            param.gbnf_str= """
                    root ::= "answer:" "{" name "," age "}"
                    name ::= " 'name:'" "lisi"
                    age  ::= " 'age:' " [0-9]+
            """;
            param.use_gbnf=true;
            llmGGufFrameWork.setSamplerAsync(
                    param,
                    context
            );
            llmGGufFrameWork.initBatchAsync(batch,finalPrompt,context);
            do {
//                System.out.println();
                var stream = new LLM_GGUF_Stream();
                llmGGufFrameWork.reasoningAsync(batch, context, stream);

                System.out.print(stream.getStream());
                System.out.flush();

            } while (batch.next_token != batch.eos);
            llmGGufFrameWork.batchFree(batch);
//            llmGGufFrameWork.destroy();
        });

        var thread2 =new Thread(()->{

            String prompt1= """
              
                有个人叫wangwu，20岁
                """;

            String finalPrompt =
                    """
                    <|im_start|>user
                    """ + prompt1 + """
                        <|im_end|>
                        <|im_start|>assistant
                        """;
            var batch = new LLM_GGUF_Batch();
            var context = new LLM_GGUF_Context();
            var param = new LLM_GGUF_Context_RTParam();
            param.gbnf_str= """
                        root ::= "answer:" "{" name "," age "}"
                        name ::= " 'name:'" "wangwu"
                        age  ::= " 'age:' " [0-9]+
                """;
            param.use_gbnf=true;
            llmGGufFrameWork.setSamplerAsync(
                    param,
                    context
            );
            llmGGufFrameWork.initBatchAsync(batch,finalPrompt,context);
            do {

                var stream = new LLM_GGUF_Stream();
//                System.out.println();
                llmGGufFrameWork.reasoningAsync(batch, context, stream);

                System.out.print(stream.getStream());
                System.out.flush();

            } while (batch.next_token != batch.eos);
            llmGGufFrameWork.batchFree(batch);
//            llmGGufFrameWork.destroy();
        });

            thread1.start();
//            thread2.start();
            thread1.join();
//            thread2.join();

    }

}
