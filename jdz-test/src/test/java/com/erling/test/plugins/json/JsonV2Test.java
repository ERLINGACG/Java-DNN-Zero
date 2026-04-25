package com.erling.test.plugins.json;

import com.erling.core.load.ann.JdzFramework;
import com.erling.core.load.jna.DyLinkLibLoader;
import com.erling.plugins.json.v2.JsonData;
import com.erling.plugins.json.v2.JsonPluginsV2;
import com.erling.plugins.json.v2.JsonPluginsV2Inf;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

public class JsonV2Test {

    @JdzFramework(
            rootPath = "E:/ZeroPlan/Java/JDnn-Zero/JDnnZero/jdz-test/libconfig/share/",
            name = "GeneralDnnLib_Zero_plugin_Json_v2",
            mapping = JsonPluginsV2Inf.class
    )
    private JsonPluginsV2 jsonPluginsV2;

    public JsonV2Test(){
        DyLinkLibLoader.Load(this);
    }


    @Test
    public void test1() throws JsonProcessingException {

        String json_str = """
                {
                  "messages":[{"content":"北京和上海哪个城市天气更好？先查一下天气。","role":"user"}],
                  "model":"你的模型名称",
                  "stream":false,
                  "temperature":0.7,
                  "tools":[
                    {
                      "type":"function",
                      "function":{
                        "name":"get_weather",
                        "description":"根据城市名称获取该地的实时天气情况。",
                        "parameters":{
                          "properties":{
                            "city":{
                              "type":"string"
                            }
                          },
                          "required":["city"],"type":"object"}
                      }
                    },
                    {
                      "type":"function",
                      "function":{
                        "name":"calculate",
                        "description":"安全地计算一个数学表达式的值。例如，输入'2+3'，返回'5'。",
                        "parameters":{
                          "properties":{
                            "expression":{"type":"string"}
                          },
                          "required":["expression"],"type":"object"}
                      }
                    }
                  ]
                }
                """;
//        ObjectMapper mapper = new ObjectMapper();
//

//
//        int iterations_2 = 1000;
//        long start_2 = System.nanoTime();
//
//        for (int i = 0; i < iterations_2; i++) {
//
//            mapper.readTree(json_str).get("messages").get(0).get("content").asText();
//
//
//        }
//        long end_2 = System.nanoTime();
//        System.out.println("JsonV2Test test2 time: " + (end_2 - start_2) / iterations_2);


        int iterations = 100000;
        long start = System.nanoTime();
        JsonData json_data = new JsonData().withJsonPluginsV2(jsonPluginsV2);
        for(int i = 0; i < iterations; i++) {

            var jsonNodeInfo = json_data.build(json_str).
                    get("messages").get(0).get("content").at();

        }
        long end = System.nanoTime();
        System.out.println("JsonV2Test test1 time: " + (end - start) / iterations);


//        System.out.println(jsonNodeInfo.getValue());
    }
}
