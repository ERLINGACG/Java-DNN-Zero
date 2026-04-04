package com.erling.jdz.llm.framework.gguf.func;

import com.erling.jdz.llm.LLmChat;
import com.erling.jdz.llm.RunTimeInit;
import com.erling.jdz.llm.ContextInit;
import com.erling.jdz.llm.framework.gguf.func.ann.FuncTools;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.lang.reflect.Method;
import java.util.*;

public class FunctionCallingBuilderGBNF<T extends LLmChat> {

    private Class<?> clazz;

    private Object   funcObj;

    List<String> functionNames = new ArrayList<>(){};

    String root = """
            root ::= "\\"" "answer" "\\"" ":" "[" functionList "]"
            functionList ::= functionName ("," functionName)*
            functionName ::= "{" "\\"" "function" "\\"" ":"  functionItem "," "\\"" "arg" "\\"" ":" "[" arg "]" "}"
            """;

   public StringBuffer function_gbnf = new StringBuffer();


    private FunctionCallingBuilderGBNF<?> GetFunctionInfo(Class<?> clazz){
        function_gbnf.append(root);
        function_gbnf.append("functionItem ::= ");
        this.clazz = clazz;
        var methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(FuncTools.class)){
                var funcName = method.getName();
                var funcArgs = method.getParameterTypes();
                function_gbnf.append("\"").
                        append("<func>").
                        append(funcName).
                        append(Arrays.toString(funcArgs)).
                        append("</func>").
                        append("\"").append("|");
            }
        }
        function_gbnf.delete(function_gbnf.length()-1, function_gbnf.length());
        function_gbnf.append("\n");
        function_gbnf.append("arg ::= [^\\]]+");
//        functionNames.forEach(System.out::println);
        return this;
    }

    public FunctionCallingBuilderGBNF<?> registerFunction(GetObj function){
        funcObj = function.get();
        return GetFunctionInfo(function.get().getClass());
    }

    T m;

    @SuppressWarnings("unchecked")
    public FunctionCallingBuilderGBNF<?> setModel(InitModel<?> model){
      m = (T) model.initModel();
      return this;
    }

    public Map<String,Object> functionRes= new HashMap<>();

    public FunctionCallingBuilderGBNF<?> build(){
        return this;
    }

    public FunctionCallingBuilderGBNF<?> chat(
            String prompt, int maxTokens,
            ContextInit<?> contextInit,
            RunTimeInit<?> runTimeInit,
            RunTimeInit<?> chatRunTimeInit
    ){
        String res = m.charExample(prompt, maxTokens,contextInit, runTimeInit,function_gbnf.toString());
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(
                   "{" + res.replace("<func>","\"").replace("</func>","\"") + "}"
                    );
            System.out.println();
            jsonNode.findValues("answer").forEach(
                    node ->{
                        if(node.isArray()){
                            for (JsonNode func : node) {
                                var funcName = func.get("function").asText();
                                var funcArgs = func.get("arg");
                                var methods = clazz.getDeclaredMethods();

                                for (Method method : methods) {
                                    method.setAccessible(true);
                                    if((method.getName()+ Arrays.toString(method.getParameterTypes())).equals(funcName)){
                                        Object[] args = new Object[funcArgs.size()];
                                        for (int i = 0; i < funcArgs.size(); i++) {
                                            switch (funcArgs.get(i).getNodeType()){
                                                case STRING:
                                                    args[i] = funcArgs.get(i).asText();
                                                    break;
                                                case BOOLEAN:
                                                    args[i] = funcArgs.get(i).asBoolean();
                                                    break;
                                                case NULL:
                                                    args[i] = null;
                                                    break;
                                                case JsonNodeType.NUMBER:
                                                    args[i] = funcArgs.get(i).asDouble();
                                                default:
                                                    args[i] = funcArgs.get(i).asInt();
                                                    break;
                                            }
                                        }
                                        try {
                                          Object resObj = method.invoke(funcObj, args);
                                          functionRes.put(funcName,"result:"+resObj.toString());
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                    }
                                }
                            }
                        }
                    }
            );

            String finalPrompt =
                    """
                    <|im_start|>user
                    你进行function calling并调用了，其格式为{函数名，结果}：
                    """ + functionRes.toString() + """
                        告诉我结果
                        <|im_end|>
                        <|im_start|>assistant
                        """
//            String finalPrompt = """
//                    <|im_start|>user
//                    你进行function calling并调用了：
//                        告诉我结果
//                        <|im_end|>
//                    """+  "<|im_start|>tool\n" + functionRes.toString() + "<|im_end|>\n" +"""
//                        <|im_start|>assistant
//                    """
            ;

            res =m.chat(finalPrompt, maxTokens,contextInit, chatRunTimeInit);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return this;
    }



    @FunctionalInterface
    public interface InitModel<T extends LLmChat>{
        T initModel();
    }

}
