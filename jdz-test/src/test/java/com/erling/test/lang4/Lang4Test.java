package com.erling.test.lang4;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
//import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Lang4Test {
     class ToolsExample{
         @Tool("返回指定城市的当前天气情况")
         public String getWeather(
                 // 2. 用 @P 注解参数，描述每个参数的含义
                 @P("需要查询天气的城市名称，例如：北京、上海") String city,
                 @P("温度单位，可选值为 'Celsius' 或 'Fahrenheit'，默认为 'Celsius'") String unit
         ) {
             // 这里是你的业务逻辑，比如调用一个真实的天气API
             return String.format("%s的当前气温是25%s", city, "Celsius".equals(unit) ? "°C" : "°F");
         }
     }


     @Test
     public void test(){
         ToolSpecification toolSpec = ToolSpecification.builder()
                 .name("getWeather")
                 .description("返回指定城市的天气预报")
                 .parameters(JsonObjectSchema.builder()
                         .addStringProperty("city", "城市名称，例如'北京'")
                         .addEnumProperty("unit", List.of("Celsius", "Fahrenheit"), "温度单位")
                         .required("city")
                         .build())
                 .build();

        System.out.println(toolSpec);
     }

     @Test
     public void test2(){
         ToolsExample weatherTools = new ToolsExample();
         List<ToolSpecification> toolSpecs = ToolSpecifications.toolSpecificationsFrom(weatherTools);
         System.out.println(toolSpecs);
     }

     @Test
     public void test3(){
       
     }

}
