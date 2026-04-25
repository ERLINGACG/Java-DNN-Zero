package com.erling.llama.llm.function;

import com.erling.llama.llm.agent.IAgent;
import com.erling.llama.llm.agent.LlamaAgent;
import com.erling.llama.llm.function.kt.FunctionObject;
import com.erling.llama.llm.function.kt.InvokeFunctionKt;
import com.erling.llama.llm.function.kt.ScanFunctionKt;
import com.erling.llama.llm.function.kt.UserToolRT;
import kotlin.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class FunctionEngine {
    Pair<String, FunctionObject> functionInfo;

    public FunctionEngine register(Object target){
        this.functionInfo = ScanFunctionKt.scanFunctionGetInfo(()->target);
        return this;
    }

    public String SendPrompt(){
        return "<tools>"+functionInfo.component1()+"</tools>";
    }

    public List<String> invoke(String result){
        var res = new ArrayList<String>();
        Pattern pattern = Pattern.compile("<tool_call>\\s*(\\{.*?})\\s*<tool_call>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(result);
        System.out.println("\n");
        while (matcher.find()){
            res.add(InvokeFunctionKt.invoke(this, matcher.group(1)));
        }
        return res;
    }





}
