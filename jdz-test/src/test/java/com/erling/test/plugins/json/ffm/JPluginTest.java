package com.erling.test.plugins.json.ffm;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.ann.JdzFramework;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
import com.erling.core.load.jna.DyLinkLibLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

public class JPluginTest {

    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GeneralDnnLib_Zero_plugin_Json_v2",
            useMappingConfig = false
    )
    public JPluginsCpp jPluginsCpp;

    public JPluginTest(){
        DyLinkLibLoaderForFFM.load(this);
    }

    @Test
    public void testJPluginsCpp() throws JsonProcessingException {
        String json = """
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
//
        int iterations_2 = 1000;
        long start_2 = System.nanoTime();
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < iterations_2; i++) {

            mapper.readTree(json).get("messages").get(0).get("content").asText();
        }
        long end_2 = System.nanoTime();
        System.out.println(STR."Time Jackson:  \{(end_2 - start_2) / iterations_2} ns");
//
//        int iterations = 1000;
//        long start = System.nanoTime();

//        for (int i = 0; i < iterations; i++) {
//            try(Arena arena = Arena.ofConfined()){
//                JsonData jsonData = new JsonData(arena)
//                        .setPluginsCppInf(jPluginsCpp.jPluginsCppInf);
//                jsonData.build(json).get("messages").get(0).get("content").at();
//            }
//
//        }
//        long end = System.nanoTime();
//        System.out.println(STR."Time JsonData: \{(end - start) / iterations} ns");

//
        int iterations = 1000;
        long start = System.nanoTime();
        try(Arena arena = Arena.ofConfined()) {
            JsonData jsonData = new JsonData(arena).setPluginsCppInf(jPluginsCpp.jPluginsCppInf);
            InfoStruct res = null;
            for (int i = 0; i < iterations; i++) {
                res = jsonData.build(json).get("messages").get(0).get("content").at();
            }
            long end = System.nanoTime();
            System.out.println(STR."Time JsonData: \{(end - start) / iterations} ns");
            if (res != null) {
                initFields(res);
                System.out.println(res.value.getForString(res.value_len.get()));
            }
        }






    }
}
