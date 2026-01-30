package cv.framework;

import com.erling.jdz.llm.framework.gguf.LLmGGufFrameWork;
import com.erling.jdz.llm.frameworkinf.gguf.LLmGGufFrameWorkInf;
import com.erling.jdz.load.DyLinkLibLoader;
import com.erling.jdz.load.RunEnv;
import com.erling.jdz.load.ann.ConfigPath;
import com.erling.jdz.load.ann.DyLinkLib;
import org.junit.jupiter.api.Test;

public class LLmGGufTest {

    @DyLinkLib(path = "E:\\ZeroPlan\\C++\\GeneralDnnLib-Zero\\cmake-build-release-visual-studio\\gdlz_test_lib\\Release\\GeneralDnnLib_Framework_Lib.dll")
    @ConfigPath("clibconf/modelconf/llm_gguf_config.json")
    public LLmGGufFrameWork llmGGufFrameWork;

    public LLmGGufTest() throws IllegalAccessException {
        RunEnv.SET_ENV.run();
        DyLinkLibLoader.load(this, LLmGGufFrameWork.class, LLmGGufFrameWorkInf.class);
    }

    @Test
    public void test(){
        llmGGufFrameWork.exampleSend();
        llmGGufFrameWork.destroy();
    }
}
