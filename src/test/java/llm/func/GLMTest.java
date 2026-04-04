package llm.func;

import com.erling.jdz.llm.framework.gguf.LLmGGufFrameWork;
import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Context;
import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Context_RTParam;
import com.erling.jdz.llm.framework.gguf.func.FunctionCalling;
import com.erling.jdz.llm.framework.gguf.func.ann.FuncTools;
import com.erling.jdz.llm.framework.gguf.models.GLM4GGuf;
import com.erling.jdz.llm.frameworkinf.gguf.LLmGGufFrameWorkInf;
import com.erling.jdz.load.DyLinkLibLoader;
import com.erling.jdz.load.RunEnv;
import com.erling.jdz.load.ann.ConfigPath;
import com.erling.jdz.load.ann.DyLinkLib;
import com.erling.jdz.load.ann.Mapping;
import org.junit.jupiter.api.Test;

public class GLMTest {

    public GLM4GGuf glm4GGuf;

    @DyLinkLib(path = "E:\\ZeroPlan\\C++\\GeneralDnnLib-Zero\\cmake-build-release-visual-studio\\gdlz_test_lib\\Release\\GeneralDnnLib_Framework_Lib.dll")
    @ConfigPath("clibconf/modelconf/llm_config/glm4.json")
    @Mapping(LLmGGufFrameWorkInf.class)
    public LLmGGufFrameWork llmGGufFrameWork;

    public GLMTest() throws IllegalAccessException {

        RunEnv.SET_ENV.run();
        DyLinkLibLoader.load(this);;
    }

    @Test
    public void Load(){

    }

    @Test
    public void chat(){
        glm4GGuf = new GLM4GGuf(() -> llmGGufFrameWork);
        String res = glm4GGuf.chat(
                "你好呀，请介绍下你自己",1024,
                LLM_GGUF_Context::new,
                ()->{
                    var rtParam = new LLM_GGUF_Context_RTParam();
                    rtParam.temp=0.8f;
                    return rtParam;
                }
        );
    }

    @Test
    public void Tools(){
        var context = new LLM_GGUF_Context();
        glm4GGuf = new GLM4GGuf(() -> llmGGufFrameWork).buildTool(
                        () -> new FunctionCalling<>().register(
                                ()->{
                                    class ToolsObj{
                                        @FuncTools(description = "加法工具")
                                        public void add(int a, int b){
                                            System.out.println();
                                            System.out.println("a:"+a+" b:"+b);
                                            System.out.println("invoke add:"+(a+b));
                                        }

                                        @FuncTools(description = "减法工具")
                                        public void sub(int a, int b){
                                            System.out.println("sub:"+a+b);
                                        }

                                        @FuncTools(description = "天气查询工具")
                                        public String getWeather(String city){
                                            return "天气晴朗";
                                        }

                                        @FuncTools(description = "地址查询工具")
                                        public String getAddress(String city){
                                            return "39°54′24″N 116°23′51″E";
                                        }

                                        @FuncTools(description = "测试工具1")
                                        public String test_1(String a, int b){
                                            System.out.println("a:"+a+" b:"+b);
                                            return "test_1:"+a+b;
                                        }
                                    }
                                    return new ToolsObj();
                                }
                        )
        ).useTool("获取北京的天气，再获取北京地址",4096,
                ()->context,
                ()->{
                    var rtParam = new LLM_GGUF_Context_RTParam();
                    rtParam.temp=0.2f;
                    return rtParam;
                }
        );

        glm4GGuf.chat(
                "所以北京今天适合旅游吗,顺便再告诉我一下你获取的地址信息",1024,
                ()->context,
                ()->{
                    var rtParam = new LLM_GGUF_Context_RTParam();
                    rtParam.temp=0.8f;
                    return rtParam;
                }
        );
    }
}
