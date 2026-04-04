package llm.func;

import com.erling.jdz.llm.framework.gguf.LLmGGufFrameWork;
import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Context;
import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Context_RTParam;
import com.erling.jdz.llm.framework.gguf.func.FunctionCalling;
import com.erling.jdz.llm.framework.gguf.func.ann.FuncTools;
import com.erling.jdz.llm.framework.gguf.models.QwenGGuf;
import com.erling.jdz.llm.frameworkinf.gguf.LLmGGufFrameWorkInf;
import com.erling.jdz.load.DyLinkLibLoader;
import com.erling.jdz.load.RunEnv;
import com.erling.jdz.load.ann.ConfigPath;
import com.erling.jdz.load.ann.DyLinkLib;
import com.erling.jdz.load.ann.Mapping;
import org.junit.jupiter.api.Test;

public class QwenTest {

    @DyLinkLib(path = "E:\\ZeroPlan\\C++\\GeneralDnnLib-Zero\\cmake-build-release-visual-studio\\gdlz_test_lib\\Release\\GeneralDnnLib_Framework_Lib.dll")
    @ConfigPath("clibconf/modelconf/llm_config/qwen.json")
    @Mapping(LLmGGufFrameWorkInf.class)
    public LLmGGufFrameWork llmGGufFrameWork;

    public QwenTest() throws IllegalAccessException {
        RunEnv.SET_ENV.run();
        DyLinkLibLoader.load(this);
    }

    @Test
    public void load(){
        QwenGGuf qwenGGuf = new QwenGGuf(()->llmGGufFrameWork);
    }

    @Test
    public void chat(){
        QwenGGuf qwenGGuf = new QwenGGuf(()->llmGGufFrameWork);
        qwenGGuf.chat("你好,请介绍你自己一下", 4096, LLM_GGUF_Context::new, LLM_GGUF_Context_RTParam::new);
    }

     @Test
     public void useTool(){
        QwenGGuf qwenGGuf = new QwenGGuf(()->llmGGufFrameWork);
        qwenGGuf.buildTool(
                ()->new FunctionCalling<>().register(()->{
                    class Tools{

                        @FuncTools(description = "这是一个加法工具")
                        public String add(int a, int b){
                            System.out.println("add "+a+" "+b);
                            return "add "+a+" "+b+" = "+(a+b);
                        }

                        @FuncTools
                        public String getWeather(String city){
                            System.out.println("getWeather "+city);
                            return "天气晴朗";
                        }

                        @FuncTools(description = "地址查询工具")
                        public String getAddress(String city){
                            return "39°54′24″N 116°23′51″E";
                        }

                    }
                    return new Tools();
                })
        ).useTool(
                "获取北京的天气,以及北京的地址", 4096,
                LLM_GGUF_Context::new, ()->{
                    var rtParam = new LLM_GGUF_Context_RTParam();
                    rtParam.n_ctx=4096;
                    rtParam.n_batch=4096;
                    return rtParam;
                }
        );
     }
}
