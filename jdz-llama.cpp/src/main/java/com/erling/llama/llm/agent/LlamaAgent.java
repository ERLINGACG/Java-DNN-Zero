package com.erling.llama.llm.agent;

import com.erling.llama.exception.LlamaFrameNull;
import com.erling.llama.llm.backend.LlamaCallBack;
import com.erling.llama.llm.backend.LlamaInvokeBackEnd;
import com.erling.llama.llm.function.FunctionEngine;
import com.erling.llama.llm.function.kt.UserToolRT;
import com.erling.llama.llm.prompt.IPromptTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class LlamaAgent<C,P> implements IAgent<LlamaAgent<C,P>,C,P> {



    private IPromptTemplate template;

    private LlamaInvokeBackEnd<C,P> frameWork;

    private FunctionEngine functionEngine;

    @NotNull
    @Override
    public LlamaAgent<C,P> setTemplate(@NotNull IPromptTemplate template) {
        this.template = template;

        return this;
    }

    @NotNull
    @Override
    public LlamaAgent<C,P> setFrameWork(@NotNull LlamaInvokeBackEnd<C,P> frameWork) {
        this.frameWork = frameWork;
        return this;
    }

    public LlamaAgent<C,P> setEngine(
            FunctionEngine functionEngine,
            Supplier<?> functionCallingSupplier
    ){
        this.functionEngine = functionEngine.register(functionCallingSupplier.get());
        return this;
    }


    @NotNull
    @Override
    public LlamaAgent<C,P> chat(
            @NotNull String prompt,boolean clear,
            @NotNull Supplier<C> rtSupplier,
            @NotNull Supplier<P> ctxSupplier,
            @NotNull LlamaCallBack recCallBack
    ) {

        return chat("",prompt,clear,rtSupplier,ctxSupplier,recCallBack);
    }

    @NotNull
    @Override
    public LlamaAgent<C,P> chat(
            @NotNull String system,
            @NotNull String prompt,boolean clear,
            @NotNull Supplier<C> rtSupplier,
            @NotNull Supplier<P> ctxSupplier,
            @NotNull LlamaCallBack recCallBack) {


        if (frameWork == null) throw new LlamaFrameNull("frameWork is null");
        if (template == null)  throw new LlamaFrameNull("template is null");

        frameWork.recording(
                template.format(system,prompt),
                template.getEndTokenID(), clear,
                rtSupplier,
                ctxSupplier,
                recCallBack);
        return this;
    }

    private boolean isDebug = false;

    public LlamaAgent<C,P> setDebug(boolean isDebug){
        this.isDebug = isDebug;
        return this;
    }

    public void useTools(Supplier<UserToolRT<C,P>> userToolRTSupplier){
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
                userToolRT.getPrompt(),true,
                userToolRT.getRtParam(),
                userToolRT.getCtx(),
                (stream, count, tokenizer) -> {

                    if(isDebug) System.out.print(stream); System.out.flush();
                    res1.append(stream);
                    return true;
                }
        );
        var invokeResult = functionEngine.invoke(res1.toString());
        var callResult = "<tools_result>"+invokeResult.toString()+"</tools_result>";
        this.chat(
                ("上一轮你调用结果为:"+callResult),
                "请告诉我结果",true,
                userToolRT.getRtParam(),
                userToolRT.getCtx(),
                userToolRT.getRecCallBack()
        );

    }


   public  float[] embeddings(
           String prompt,boolean clear,
           Supplier<C> ctxSupplier,
           Supplier<P> paramsSupplier
           ){
        return frameWork.embeddings(
                prompt,clear,
                ctxSupplier,
                paramsSupplier
        );
   }


}
