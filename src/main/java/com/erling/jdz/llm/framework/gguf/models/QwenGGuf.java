package com.erling.jdz.llm.framework.gguf.models;

import com.erling.jdz.llm.ContextInit;
import com.erling.jdz.llm.RunTimeInit;
import com.erling.jdz.llm.framework.gguf.LLmGGufFrameWork;
import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Batch;
import com.erling.jdz.llm.framework.gguf.func.FunctionCalling;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.function.Supplier;

public class QwenGGuf {

    final LLmGGufFrameWork frameWork;


    public QwenGGuf(Supplier<LLmGGufFrameWork> frameWorkSupplier){
        this.frameWork = frameWorkSupplier.get();
    }

   public String System_prompt_set = """
            """;
    StringBuffer System_prompt = new StringBuffer();

    public FunctionCalling<?> functionCalling;

    public QwenGGuf buildTool(Supplier<FunctionCalling<?>> functionCallingSupplier){
        this.functionCalling = functionCallingSupplier.get();
        return this;
    }

    public QwenGGuf useTool(String prompt, int maxTokens, ContextInit<?> context, RunTimeInit<?> runTimeInit){
        System_prompt.append("你可以使用以下函数");
        System_prompt.append("<tools>").append(functionCalling.systemPrompt()).append("</tools>");
        System_prompt.append("""
                对于每个函数调用，返回一个包含函数名称和参数的 JSON 对象，并将其放在 <tool_call><tool_call> 标签内，例如：
                <tool_call>{"name": "function_name", "arguments": {"arg1": "value"}}<tool_call>
                """);
        String input_prompt = """
             <|im_start|>system
             """+ System_prompt_set +"""
             """+ System_prompt +"""
             <|im_end|>
             <|im_start|>user
            """+prompt+"""
            <|im_start|>assistant
            """;


       String result = frameWork.chat(
                input_prompt, maxTokens,
                context, runTimeInit
        );
        try {
            var resultMap = functionCalling.invokeToJson(result);
            String result_json = "<tool_response>"+resultMap.toString()+"</tool_response>";
            String output_prompt = """
                 <|im_start|>system
                 """+ System_prompt_set +"""
                 你调用了函数的结果为：
                 """+ result_json +"""
                 <|im_end|>
                 <|im_start|>user
                 告诉我结果
                 <|im_start|>assistant
                """;
             frameWork.chat(
                    output_prompt, maxTokens,
                    context, runTimeInit
             );

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public String chat(String prompt, int maxTokens, ContextInit<?> context, RunTimeInit<?> runTimeInit){

        String input_prompt = """
             <|im_start|>system
             """+ System_prompt_set +"""
             """+System_prompt.toString()+"""
             <|im_end|>
             <|im_start|>user
            """+prompt+"""
            <|im_start|>assistant
            """;
        return frameWork.chat(
                input_prompt, maxTokens,
                context, runTimeInit
        );
    }
}
