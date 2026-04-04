package com.erling.jdz.llm.framework.gguf.func;

import com.erling.jdz.llm.framework.gguf.func.ann.FuncTools;
import com.erling.jdz.llm.framework.gguf.func.ann.TryInvoke;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

public class FunctionCallingExample {

    private final Object function;

    private final Logger logger = Logger.getLogger(FunctionCallingExample.class.getName());

    public FunctionCallingExample(Object function){
        this.function = function;
    }

    public void ReflectionGetFunC(){
        Class<?> clazz = function.getClass();
        var methods = clazz.getDeclaredMethods();
        for(var method : methods){


            if(method.isAnnotationPresent(FuncTools.class)){
                System.out.println(method.getName());
                System.out.println(method.getName() + Arrays.toString(method.getParameterTypes()));
            }
        }
    }




    public String ReflectionGetToGBNF(){
        StringBuilder gbnf=new StringBuilder();
        String root= """
                 root ::= "\\"" "answer" "\\"" ":" "{" "\\"" "functionName" "\\"" ":" functionItem  "," "\\"" "arg" "\\"" ":" "[" arg "]" "}"
                 """;
        String functionItem= """
                  functionItem ::=""";
        gbnf.append(root);
        gbnf.append(functionItem);
        Class<?> clazz = function.getClass();
        var methods = clazz.getDeclaredMethods();
        for(var method : methods){
                if(method.isAnnotationPresent(FuncTools.class)){
                   gbnf.
                        append('"').
                           append("<func>").
                           append(method.getName()).
                           append(Arrays.toString(method.getParameterTypes())).
                           append("</func>").
                           append('"');
                   gbnf.append("|");
                }
        }
        gbnf.deleteCharAt(gbnf.length()-1);
        gbnf.append("\n");
        String arg= """ 
               arg ::= [^\\[\\]]+""";
        gbnf.append(arg);
        return gbnf.toString();
    }

    public void InvokeFunc(Object... obj) {
        Class<?> clazz = function.getClass();
        var methods = clazz.getDeclaredMethods();
        for(var method : methods){
            if(method.isAnnotationPresent(TryInvoke.class)){
                try {
                    method.invoke(function, obj);
                } catch (Exception e) {
                    logger.severe("InvokeFunc error: " + e.getMessage());
                }
            }
        }
    }

    private Object[] toObjectArray(int[] array) {
        Integer[] result = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public Map<String,Object> InvokeFunc(JsonNode jsonNode) {

        Object ret = new Object();
        String functionName = jsonNode.get("functionName").asText();
        JsonNode argArray = jsonNode.get("arg");

        Object[] args = new Object[argArray.size()];
        for(int i = 0; i < argArray.size(); i++){
            switch (argArray.get(i).getNodeType()){
                case STRING:
                    args[i] = argArray.get(i).asText();
                    break;
                case BOOLEAN:
                    args[i] = argArray.get(i).asBoolean();
                    break;
//                case INT:
//                    args[i] = argArray.get(i).asDouble();
//                    break;
                case NULL:
                    args[i] = null;
                    break;
                default:
                    args[i] = argArray.get(i).asInt();
                    break;
            }
        }
        String methodName = "";
        Class<?> clazz = function.getClass();
        var methods = clazz.getDeclaredMethods();
        for(var method : methods){
            if(method.isAnnotationPresent(FuncTools.class)){
                String methodDesc = method.getName() + Arrays.toString(method.getParameterTypes());
                if(methodDesc.equals(functionName)){
                    try {
//                        System.out.println("InvokeFunc: " + methodDesc);
                        methodName = methodDesc;
                        if(Arrays.toString(method.getParameterTypes()).equals("[]")){
                           ret= method.invoke(function);
                        }else{
                           ret= method.invoke(function, args);
                        }
                    } catch (Exception e) {
                        logger.severe("InvokeFunc error: " + e.getMessage());
                    }
                }
            }
        }
        return Map.of(methodName+" res", ret);
    }
}
