package llm.func;

import com.erling.jdz.llm.framework.gguf.LLmGGufFrameWork;
import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Batch;
import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Context;
import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Context_RTParam;
import com.erling.jdz.llm.framework.gguf.func.FunctionCallingBuilderGBNF;
import com.erling.jdz.llm.framework.gguf.func.FunctionCallingExample;
import com.erling.jdz.llm.framework.gguf.func.ann.FuncTools;
import com.erling.jdz.llm.framework.gguf.stream.LLM_GGUF_Stream;
import com.erling.jdz.llm.frameworkinf.gguf.LLmGGufFrameWorkInf;
import com.erling.jdz.load.DyLinkLibLoader;
import com.erling.jdz.load.RunEnv;
import com.erling.jdz.load.ann.ConfigPath;
import com.erling.jdz.load.ann.DyLinkLib;
import com.erling.jdz.load.ann.Mapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionCallTest {

//    @FuncToos
    public void test_func1(int a,int b){
        System.out.println("func1: " + a + " " + b);
    }

//    @FuncToos
    public void test_func2(String a,double b){
        System.out.println("func2: " + a + " " + b);
    }


//    @FuncToos
    public void readFileDetail(String path){
        System.out.println("readFile: " + path);
    }

    @FuncTools
    public void deleteFile(String path){
        System.out.println("deleteFile: " + path);
    }

    @FuncTools
    public void execCmd(String cmd){
        System.out.println("execCmd: " + cmd);
    }

    @FuncTools
    public int add(int a,int b){
        return a+b;
    }

    @FuncTools
    public Map<String,Object> getCityPosition(String city){
        return Map.of("city",city,"position","116.403874,39.904211");
    }

    @FuncTools
    public String getWeather(String city){
        System.out.println("getWeather: " + city);
        return "天气晴朗";
    }

    @DyLinkLib(path = "E:\\ZeroPlan\\C++\\GeneralDnnLib-Zero\\cmake-build-release-visual-studio\\gdlz_test_lib\\Release\\GeneralDnnLib_Framework_Lib.dll")
    @ConfigPath("clibconf/modelconf/llm_gguf_config.json")
    @Mapping(LLmGGufFrameWorkInf.class)
    public LLmGGufFrameWork llmGGufFrameWork;

    public FunctionCallTest() throws IllegalAccessException {

        RunEnv.SET_ENV.run();
        DyLinkLibLoader.load(this);
    }

    @Test
    public void testReflectionGetFunC() throws JsonProcessingException {
        String prompt1= """
               获取北京位置
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

        var functionCalling = new FunctionCallingExample(this);
        param.gbnf_str= functionCalling.ReflectionGetToGBNF();
        System.out.println(param.gbnf_str);
        param.use_gbnf=true;
        llmGGufFrameWork.setSamplerAsync(
                param,
                context
        );
        llmGGufFrameWork.initBatchAsync(batch,finalPrompt,context);
        StringBuffer result = new StringBuffer();
        do {
            var stream = new LLM_GGUF_Stream();
            llmGGufFrameWork.reasoningAsync(batch, context, stream);
            System.out.print(stream.getStream());
            result.append(stream.getStream());

        } while (batch.next_token != batch.eos);
        llmGGufFrameWork.batchFree(batch);
//        llmGGufFrameWork.destroy();
        System.out.println();
        String replace = result.toString().replace("<func>", "\"").replace("</func>", "\"");
        System.out.println("json: " + replace);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(
                "{"+ replace +"}"
        );
//        System.out.println("Node type: " + rootNode.getNodeType());
//        System.out.println("Node content: " + rootNode.toPrettyString());
        AtomicReference<String> cal = new AtomicReference<>("");
        functionCalling.InvokeFunc(rootNode.get("answer")).forEach((k,v)->{
//            System.out.println(k + " " + v);
            cal.set(k+":"+v);
        });
        System.out.println("cal: " + cal.get());




        String prompt2= cal.get()+"""
     
        """;
        String finalPrompt2 =
                """
                <|im_start|>user
                """ + prompt2 + """
                        <|im_end|>
                        <|im_start|>assistant
                        """;


        var param2 = new LLM_GGUF_Context_RTParam();
        var batch2 = new LLM_GGUF_Batch();
        param2.use_gbnf=false;
        llmGGufFrameWork.setSamplerAsync(
                param2,
                context
        );
        llmGGufFrameWork.initBatchAsync(batch2,prompt2,context);
        do {
            var stream = new LLM_GGUF_Stream();
            llmGGufFrameWork.reasoningAsync(batch2, context, stream);
            System.out.print(stream.getStream());

        } while (batch2.next_token != batch2.eos);
        llmGGufFrameWork.batchFree(batch2);

//           System.out.println(rootNode.get("answer").get("functionName"));
//        llmGGufFrameWork.json_ExampleSend("{"+result.toString().replace('\'', '"')+"}");


    }


    @Test
    public void testReflectionGetFunC2Step() throws JsonProcessingException {

        var batch = new LLM_GGUF_Batch();
        var context = new LLM_GGUF_Context();
        var param = new LLM_GGUF_Context_RTParam();
        param.use_gbnf=true;
//        param.gbnf_str= """
//                root ::= "answer:" "{" name "," age "}"
//                                name ::= " 'name:'" [^,]+
//                                age  ::= " 'age:' " [0-9]+
//                """;
        String test_gbnf= """
                root ::= "\\"" "answer" "\\"" ":" "[" functionList "]"
                functionList ::= functionName ("," functionName)*
                functionName ::= "{" "\\"" "function" "\\"" ":"  functionItem "," "\\"" "arg" "\\"" ":" "[" arg "]" "}"
                functionItem ::=
                "<func>getLocation[String]</func>" |
                "<func>getWeather[String]</func>" |
                "<func>getTemperature[String]</func>"
                arg ::= [^\\]]+
                """;
        param.gbnf_str= test_gbnf;
        llmGGufFrameWork.setSamplerAsync(
                param,
                context
        );

        String prompt1= """
              获取北京的位置和天气
               """;
        String finalPrompt =
                """
                <|im_start|>user
                """ + prompt1 + """
                        <|im_end|>
                        <|im_start|>assistant
                        """;
        StringBuffer result = new StringBuffer();
        llmGGufFrameWork.initBatchAsync(batch,finalPrompt,context);
        do {
                var stream = new LLM_GGUF_Stream();
                llmGGufFrameWork.reasoningAsync(batch, context, stream);
//                System.out.print(stream.getStream());
                result.append(stream.getStream());
        } while (batch.next_token != batch.eos);
        llmGGufFrameWork.batchFree(batch);
        System.out.println(result);
//        System.out.println(result.toString().replace("<func>","\"").replace("</func>","\""));
        ObjectMapper objectMapper = new ObjectMapper();
         JsonNode rootNode = objectMapper.readTree(
                "{"+ result.toString().replace("<func>","\"").replace("</func>","\"") +"}"
         );
         System.out.println(rootNode.toPrettyString());
    }


    @Test
    public void testReflectionGetFunC3Step() {
        String prompt1= """
                [gMASK]<sop><|system|>
                You are a helpful AI assistant.
                <|user|>
                请介绍一下你自己。
                <|assistant|>
               """;
        String finalPrompt =
                """
<|im_start|>system
\\no think You have access to the following functions:
<tools>
{
  "name": "get_weather",
  "description": "获取某个地点的天气",
  "parameters": {
    "type": "object",
    "properties": {
      "res": {"type": "string"}
    },
    "required": ["res"]
  }
},
{
    "name": "calculate_sum",
    "description": "计算一组数字的总和",
    "parameters": {
        "type": "object",
        "properties": {
            "numbers": {
                "type": "array",
                "items": {"type": "number"},
               
            },
            "precision": {
                "type": "integer",
                "default": 2
            }
        },
        "required": ["numbers"]
    }
}
</tools>
For each function call, return a JSON object with function name and arguments inside <tool_call></tool_call> tags, e.g.:
<tool_call>{"name": "function_name", "arguments": {"arg1": "value"}}</tool_call>
<|im_end|>
<|im_start|>user
请使用工具 500+100等于多少
<|im_end|>
<|im_start|>assistant
""";

        var context = new LLM_GGUF_Context();
        llmGGufFrameWork.chat(
                prompt1,
                1024,
                () -> context,
                () -> {
                    var param = new LLM_GGUF_Context_RTParam();
                    param.temp=0.5f;
                    param.use_gbnf=false;
                    param.gbnf_str= """
                            root ::= "answer:" "{" name "," age "}"
                            name ::= " 'name:'" [^,]+
                            age  ::= " 'age:' " [0-9]+
                            """;
                    return param;
                }

        );
    }

    @Test
    public void testReflectionGetFunC4() throws JsonProcessingException {
//        var context = new LLM_GGUF_Context();
        String prompt1= """
              读取当前路径下的./hello/hello.txt文件内容
              """;
        String finalPrompt =
                """
                <|im_start|>user
                """ + prompt1 + """
                        <|im_end|>
                        <|im_start|>assistant
                        """;
//        var param = new LLM_GGUF_Context_RTParam();
        var builder = new FunctionCallingBuilderGBNF<LLmGGufFrameWork>().
        registerFunction(
                ()->{
                    class  TestFunc{
                        @FuncTools
                        public String getLocation(String city){
                            System.out.println("getLocation: " + city);
                            return "39°54′24″N 116°23′51″E";
                        }

                        @FuncTools
                        public String getWeather(String city){
                            System.out.println("getWeather: " + city);
                            return "晴";
                        }

                        @FuncTools
                        public String getTemperature(String city){
                            System.out.println("getTemperature: " + city);
                            return "25";
                        }

                        @FuncTools
                        public int add(int a, int b){
                            return a+b;
                        }

                        @FuncTools
                        public String readFile(String path){
                            System.out.println("readFile: " + path);
                            return "这是一个txt";
                        }
                    }
                    return new TestFunc();
                }
        ).setModel(()->llmGGufFrameWork).build().
        chat(finalPrompt,1024,
                LLM_GGUF_Context::new,
                ()->{
                  var invoke_param = new LLM_GGUF_Context_RTParam();
                  invoke_param.temp=0.1F;
                  return invoke_param;
                },
                ()->{
                   var answer_chat_param = new LLM_GGUF_Context_RTParam();
                   answer_chat_param.temp=0.3F;
                   return answer_chat_param;
                }
        );
//                        () -> {
//                    var param = new LLM_GGUF_Context_RTParam();
//                    param.temp=0.1f;
//                    param.use_gbnf=false;
//                    param.gbnf_str= """
//                            root ::= "answer:" "{" name "," age "}"
//                            name ::= " 'name:'" [^,]+
//                            age  ::= " 'age:' " [0-9]+
//                            """;
//                    return param;
//                }
    }


    @Test
    public void testEmbeddings(){
        String prompt1= """
               test1""";

        var batch = new LLM_GGUF_Batch();
        var context = new LLM_GGUF_Context();
        var param = new LLM_GGUF_Context_RTParam();
        param.use_embeddings=1;
        llmGGufFrameWork.setSamplerAsync(param,context);
        llmGGufFrameWork.initEmbeddings(batch,prompt1,context);
    }



    @Test
    public void testQwen(){
        var prompt1= """
               你好,请介绍你自己
               """;

        String finalPrompt =
                """
                <|im_start|>user
                """ + prompt1 + """
                        <|im_end|>
                        <|im_start|>assistant
                        """;
        var context = new LLM_GGUF_Context();
        llmGGufFrameWork.chat(
                finalPrompt,
                1024,
                () -> context,
                () -> {
                    var param = new LLM_GGUF_Context_RTParam();
                    param.temp=0.5f;
                    param.use_gbnf=false;
                    return param;
                });
    }

    @Test
    public void testGlm() throws JsonProcessingException {
        var prompt1= """
                [gMASK]<sop><|system|>
                你是一个名为 ChatGLM 的人工智能助手。
                 {
                         "type": "function",
                         "function": {
                             "name": "get_weather",
                             "description": "获取指定城市的当前天气信息",
                             "parameters": {
                                 "type": "object",
                                 "properties": {
                                     "args": {
                                         "type": "string",
                                         "description": "城市名称，例如：北京、上海"
                                     }
                                 },
                                 "required": ["result"]
                             }
                         }
                },
                {
                         "type": "function",
                         "function": {
                             "name": "add_nums",
                             "description": "计算两个整数的和",
                             "parameters": {
                                 "type": "array",
                                 "properties": {
                                     "args": {
                                         "type": "integer",
                                         "description": "整数列表"
                                     }
                                 },
                                 "required": ["result"]
                             }
                         }
                }
                在调用上述函数时，请使用
                <tool_call> {"name": "function_name", "arguments": {"arg1": "value"}} </tool_call> <|user|>
                格式表示调用输出。
                <|user|>
                1+2等于多少
                <|assistant|>
               """;

        String finalPrompt =
                """
                <|im_start|>user
                """ + prompt1 + """
                        <|im_end|>
                        <|im_start|>assistant
                        """;
        var context = new LLM_GGUF_Context();
        var response = llmGGufFrameWork.chat(
                prompt1,
                1024,
                () -> context,
                () -> {
                    var param = new LLM_GGUF_Context_RTParam();
                    param.temp=0.1f;
                    param.use_gbnf=false;
                    return param;
                },
                151336
        );
        Pattern pattern = Pattern.compile("<tool_call>\\s*(\\{.*?})\\s*</tool_call>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            String json = matcher.group(1); // 提取到的 JSON 字符串
            JsonNode jsonNode = new ObjectMapper().readTree(json);
            System.out.println(jsonNode.toPrettyString());
            // 输出：{"name": "get_weather", "arguments": {"city": "北京"}}
        }

    }

}
