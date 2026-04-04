package com.erling.jdz.llm.framework.gguf.models;

import com.erling.jdz.llm.ContextInit;
import com.erling.jdz.llm.RunTimeInit;
import com.erling.jdz.llm.framework.gguf.LLmGGufFrameWork;
import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Context_RTParam;
import com.erling.jdz.llm.framework.gguf.func.FunctionCalling;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;

import javax.swing.plaf.PanelUI;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GLM4GGuf {



    public GLM4GGuf setSystem_prompt(String system_prompt) {
        System_prompt.append(system_prompt);
        return this;
    }

    public StringBuffer System_prompt= new StringBuffer();

    public String System_prompt_set ="";

    public FunctionCalling<?> functionCalling;

    public LLmGGufFrameWork frameWork;

    public GLM4GGuf(Supplier<LLmGGufFrameWork> frameWorkSupplier){
         this.frameWork = frameWorkSupplier.get();
    }

    public GLM4GGuf buildTool(Supplier<FunctionCalling<?>> functionCallingSupplier){
        functionCalling = functionCallingSupplier.get();
        return this;
    }

    public GLM4GGuf useTool(String prompt, int maxTokens, ContextInit<?> context, RunTimeInit<?> runTimeInit){
        System_prompt.append("""
                你可以使用以下函数：
                """);
        System_prompt.append(functionCalling.systemPrompt());
        System_prompt.append("""
                在调用上述函数时，请使用
                <tool_call> {"name": "function_name", "arguments": {"arg1": "value"}} <tool_call>
                格式表示调用输出。
                """);
        String input_prompt = """
             [gMASK]<sop><|system|>"""+ System_prompt_set +"""
             """+ System_prompt +"""
             <|user|>
            """+prompt+"""
            <|assistant|>
            """;
        System.out.println(input_prompt);
        String result = this.chat(input_prompt, maxTokens, context, runTimeInit);
        try {
            var resultMap = functionCalling.invokeToJson(result);
            String result_json = "<tool_response>"+resultMap.toString()+"</tool_response>";
            String result_prompt ="""
                 [gMASK]<sop><|system|>"""+ System_prompt_set +"""
                 你调用得到了：
                 """+ result_json +"""
                 <|user|>
                请告诉我结果
                <|assistant|>
                """;
            String result_2 = this.chat(result_prompt, maxTokens, context, runTimeInit);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public String chat(String prompt, int maxTokens, ContextInit<?> context, RunTimeInit<?> runTimeInit){

        String input_prompt = """
             [gMASK]<sop><|system|>"""+ System_prompt_set +"""
             """+System_prompt.toString()+"""
             <|user|>
            """+prompt+"""
            <|assistant|>
            """
                ;
        return frameWork.chat(
                input_prompt, maxTokens,
                context, runTimeInit,
                151336
        );
    }
}
