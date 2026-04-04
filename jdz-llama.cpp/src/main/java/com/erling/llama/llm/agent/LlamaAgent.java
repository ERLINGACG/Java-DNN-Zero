package com.erling.llama.llm.agent;

import com.erling.llama.exception.LlamaFrameNull;
import com.erling.llama.llm.framework.LlamaCppFrameWork;
import com.erling.llama.llm.framework.RecCallback;
import com.erling.llama.llm.function.FunctionEngine;
import com.erling.llama.llm.function.kt.UserToolRT;
import com.erling.llama.llm.prompt.IPromptTemplate;
import com.erling.llama.llm.struct.LLM_GGUF_Context;
import com.erling.llama.llm.struct.LLM_GGUF_Context_RTParam;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class LlamaAgent implements IAgent<LlamaAgent> {

    private IPromptTemplate template;

    private LlamaCppFrameWork frameWork;

    private FunctionEngine functionEngine;

    @NotNull
    @Override
    public LlamaAgent setTemplate(@NotNull IPromptTemplate template) {
        this.template = template;

        return this;
    }

    @NotNull
    @Override
    public LlamaAgent setFrameWork(@NotNull LlamaCppFrameWork frameWork) {
        this.frameWork = frameWork;
        return this;
    }

    public LlamaAgent setEngine(
            FunctionEngine functionEngine,
            Supplier<?> functionCallingSupplier
    ){
        this.functionEngine = functionEngine.register(functionCallingSupplier.get());
        return this;
    }


    @NotNull
    @Override
    public LlamaAgent chat(
            @NotNull String prompt,
            @NotNull Supplier<LLM_GGUF_Context_RTParam> rtSupplier,
            @NotNull Supplier<LLM_GGUF_Context> ctxSupplier,
            @NotNull RecCallback recCallback
    ) {

        return chat("",prompt,rtSupplier,ctxSupplier,recCallback);
    }

    @NotNull
    @Override
    public LlamaAgent chat(
            @NotNull String system,
            @NotNull String prompt,
            @NotNull Supplier<LLM_GGUF_Context_RTParam> rtSupplier,
            @NotNull Supplier<LLM_GGUF_Context> ctxSupplier,
            @NotNull RecCallback recCallback) {


        if (frameWork == null) throw new LlamaFrameNull("frameWork is null");
        if (template == null)  throw new LlamaFrameNull("template is null");

        frameWork.recording(
                template.format(system,prompt),
                template.getEndTokenID(),
                rtSupplier,
                ctxSupplier,
                recCallback
        );
        return this;
    }

    private boolean isDebug = false;

    public LlamaAgent setDebug(boolean isDebug){
        this.isDebug = isDebug;
        return this;
    }

    public void useTools(Supplier<UserToolRT> userToolRTSupplier){
        if (functionEngine == null) throw new LlamaFrameNull("functionEngine is null");
        var userToolRT = userToolRTSupplier.get();
        String baseSystem = userToolRT.getSystem() +
                " | 你可以使用：" + functionEngine.SendPrompt() +
                "\n"+
                """
                对于每个函数调用，返回一个包含函数名称和参数的 JSON 对象，并将其放在 <tool_call><tool_call> 标签内，例如：
                <tool_call>{"name": "function_name", "arguments": {"arg1": "value"}}<tool_call>
                """;
        var res1 = new StringBuffer();
        this.chat(
                baseSystem,
                userToolRT.getPrompt(),
                userToolRT.getRtParam(),
                userToolRT.getCtx(),
                (stream, count) -> {
                    if(count>=userToolRT.component3().get().n_ctx) return false;
                    if(isDebug) System.out.print(stream); System.out.flush();
                    res1.append(stream);
                    return true;
                }
        );
        var invokeResult = functionEngine.invoke(res1.toString());
        var callResult = "<tools_result>"+invokeResult.toString()+"</tools_result>";
        this.chat(
                ("上一轮你调用结果为:"+callResult),
                "请告诉我结果",
                userToolRT.getRtParam(),
                userToolRT.getCtx(),
                userToolRT.getRecCallback()
        );

//        System.out.println(functionEngine.SendPrompt());
    }
}
