package llm.func;

import com.erling.jdz.llm.framework.gguf.LLmGGufFrameWork;
import com.erling.jdz.llm.framework.gguf.func.FunctionCalling;
import com.erling.jdz.llm.framework.gguf.func.ann.FuncTools;
import com.erling.jdz.llm.frameworkinf.gguf.LLmGGufFrameWorkInf;
import com.erling.jdz.load.DyLinkLibLoader;
import com.erling.jdz.load.RunEnv;
import com.erling.jdz.load.ann.ConfigPath;
import com.erling.jdz.load.ann.DyLinkLib;
import com.erling.jdz.load.ann.Mapping;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

public class FunctionCallJsonTest {

    @DyLinkLib(path = "E:\\ZeroPlan\\C++\\GeneralDnnLib-Zero\\cmake-build-release-visual-studio\\gdlz_test_lib\\Release\\GeneralDnnLib_Framework_Lib.dll")
    @ConfigPath("clibconf/modelconf/llm_gguf_config.json")
    @Mapping(LLmGGufFrameWorkInf.class)
    public LLmGGufFrameWork llmGGufFrameWork;


    public FunctionCallJsonTest() throws IllegalAccessException {
        RunEnv.SET_ENV.run();
        DyLinkLibLoader.load(this);
    }

    @Test
    @Description("加载测试")
    public void Load(){

    }


    @Test
    @Description("函数注册")
    public void register() {
        var func = new FunctionCalling<LLmGGufFrameWork>().register(()->{
            class TestTools{
                @FuncTools(
                        description = "这是一个测试函数工具"
                )
                public void testFunc(int a,String b){
                    System.out.println("testFunc");
                }

                @FuncTools(
                        description = "这是一个测试函数工具"
                )
                public void testFunc2(int a,String b){
                    System.out.println("testFunc2");
                }
            }
            return new TestTools();
        }).build(this.llmGGufFrameWork).chat();
    }
}
