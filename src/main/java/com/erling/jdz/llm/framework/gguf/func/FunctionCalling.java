package com.erling.jdz.llm.framework.gguf.func;

import com.erling.jdz.llm.LLmChat;
import com.erling.jdz.llm.framework.gguf.func.ann.FuncTools;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonArray;

import java.lang.reflect.InvocationTargetException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FunctionCalling<T extends LLmChat> {


    public FunctionCalling() {
        initFuncDescription();
    }

    public FunctionCalling<T> build(T llmChat){

        return this;
    }



   ArrayNode funcList;

    public StringBuffer systemPrompt() {
        systemPrompt.append(funcList.toPrettyString());
        return systemPrompt;
    }

    StringBuffer systemPrompt=new StringBuffer();

    private void initFuncDescription(){

        ObjectMapper mapper = new ObjectMapper();
//        this.funcDescription =
        this.funcList = mapper.createArrayNode();

    }

    Object target;

    public FunctionCalling<T> register(RegisterFunc registerFunc) {

        var funcOBJ = registerFunc.register();
        this.target = funcOBJ;
        Class<?> funcClass = funcOBJ.getClass();
        var methods = funcClass.getDeclaredMethods();

        for (var method : methods) {
            method.setAccessible(true);

            if(method.isAnnotationPresent(FuncTools.class)){
                ObjectNode funcDescription= new ObjectMapper().createObjectNode();;
                var paramsNode = new ObjectMapper().createObjectNode();
                paramsNode.put("type","object");

                var paramsList = new ObjectMapper().createObjectNode();
                var requiredArgsList = new ObjectMapper().createArrayNode();

                int args= 0;
                for (var parameter : method.getParameters()) {
                    String arg_str= "arg"+args++;
                    var paramNode = new ObjectMapper().createObjectNode();
                    paramNode.put("type",parameter.getType().getSimpleName());
                    paramsList.set(arg_str,paramNode);
                    requiredArgsList.add(arg_str);
                }

                paramsNode.set("properties",paramsList);
                paramsNode.set("required",requiredArgsList);

                var funcNode = new ObjectMapper().createObjectNode();
                funcNode.put("name",method.getName());
                funcNode.put("description",method.getAnnotation(FuncTools.class).description());
                funcNode.set("parameters",paramsNode);

                funcDescription.put( "type", "function");
                funcDescription.set("function",funcNode);
                funcList.add(funcDescription);
            }
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    public ArrayNode invokeToJson(String llmRes) throws JsonProcessingException {

        Pattern pattern = Pattern.compile("<tool_call>\\s*(\\{.*?})\\s*<tool_call>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(llmRes);
        ArrayNode callList = new ObjectMapper().createArrayNode();
        while (matcher.find()){
            JsonNode funcCallList = new ObjectMapper().readTree(matcher.group(1));
            callList.add(funcCallList);
        }

        ArrayNode resultList = new ObjectMapper().createArrayNode();
        callList.forEach(call -> {
            var funcName = call.get("name").asText();
            var args = call.get("arguments");

            Map<String,Object> argsMap;
            try {
                argsMap = new ObjectMapper().readValue(args.toString(), Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            Object[] argsArray = argsMap.values().toArray();
            for (var methods : target.getClass().getDeclaredMethods()) {
                methods.setAccessible(true);
                if (methods.getName().equals(funcName)){
                    var resNode =new ObjectMapper().createObjectNode();;
                    try {
                        var resObj=methods.invoke(target, argsArray);


                        resNode.put("name",funcName);
                        if(resObj==null){
                            resNode.put("result","void");
                        }else{
                            resNode.put("result", resObj.toString());
                        }

                        resultList.add(resNode);

                    } catch (IllegalAccessException | InvocationTargetException e) {
                        resNode.put("name",funcName);
                        resNode.put("result", e.toString());
                        resultList.add(resNode);
                        throw new RuntimeException(e);
                    }
                }
            }
        });
//
        return resultList;
    }


    @FunctionalInterface
    public interface RegisterFunc{
        Object register();
    }



    public FunctionCalling<T> chat(){

        System.out.println(funcList.toPrettyString());
        return this;
    }
}
