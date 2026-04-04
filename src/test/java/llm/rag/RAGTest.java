package llm.rag;

import com.erling.jdz.llm.framework.gguf.LLmGGufFrameWork;
import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Context;
import com.erling.jdz.llm.framework.gguf.batch.LLM_GGUF_Context_RTParam;
import com.erling.jdz.llm.frameworkinf.gguf.LLmGGufFrameWorkInf;
import com.erling.jdz.load.DyLinkLibLoader;
import com.erling.jdz.load.RunEnv;
import com.erling.jdz.load.ann.ConfigPath;
import com.erling.jdz.load.ann.DyLinkLib;
import com.erling.jdz.load.ann.Mapping;
import org.junit.jupiter.api.Test;

public class RAGTest {

    @DyLinkLib(path = "E:\\ZeroPlan\\C++\\GeneralDnnLib-Zero\\cmake-build-release-visual-studio\\gdlz_test_lib\\Release\\GeneralDnnLib_Framework_Lib.dll")
    @ConfigPath("clibconf/modelconf/llm_config/qwen.json")
    @Mapping(LLmGGufFrameWorkInf.class)
    public LLmGGufFrameWork llmGGufFrameWork;

    public RAGTest() throws IllegalAccessException {
        RunEnv.SET_ENV.run();
        DyLinkLibLoader.load(this);
    }

    @Test
    public void load(){}

    @Test
    public void InitEmbedding(){
        llmGGufFrameWork.embeddings(
                "你好请介绍你自己一下",

                ()->{
                    var param = new LLM_GGUF_Context_RTParam();
                    param.use_embeddings=1;
                    return param;
                },
                LLM_GGUF_Context::new
        );
    }
}
