package com.erling.test.tokens;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
import com.erling.core.load.ffm.api.cpp.hook.SetConfig;
import com.erling.core.load.jna.DyLinkLibLoader;
import com.erling.tokens.framework.TokensFramework;
import com.erling.tokens.struct.TokenizerIDS;
import com.erling.tokens.struct.TokenizerLoader;
import com.erling.tokens.struct.TokenizerTokens;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;
import java.util.Arrays;

public class BERT_TokenizeTest {

    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GDLZTokens",
            useMappingConfig = false
    )
    @SetConfig(
            args = {
                    "E:\\ZeroPlan\\Java\\JDnn-Zero\\JDnnZero\\jdz-test\\libconfig\\onnx\\glm_tokenzer.json"
            }
    )
    public TokensFramework tokensFramework;

    public BERT_TokenizeTest(){

        SetRunTimeEnv.SET.run();
        DyLinkLibLoaderForFFM.load(this);
    }


    @Test
    public void testLoader(){
        try(Arena arena = Arena.ofShared()){
            var ids = new TokenizerIDS(arena);
            var tokens = new TokenizerTokens(arena);

            var tokenizerLoader = new TokenizerLoader(arena);
            tokenizerLoader.setIFramework(tokensFramework);
            long[] token_s={972, 906, 2871, 5271, 24851, 54869, 14328, 17200, 3508, 8715, 1812, 2122, 300, 8007, 3508, 8715, 21208, 15889, 649, 32, 50, 48, 50, 51, 32, 6951, 8163, 36552, 6073, 15430, 77, 45, 52, 32, 4293, 28303, 2871, 1111, 365, 819, 54194, 650, 36433, 2807, 12116, 396, 460, 1345, 14776, 10735, 9984, 2509, 1126, 3069, 5012, 17241, 1401, 8355, 9474, 326, 59253};
//            System.out.println(Arrays.toString(tokenizerLoader.encodeForLong(ids, """
//                    [gMASK]<sop><|system|>你是一个有用的AI助手。
//                    <|user|>你好，请介绍你自己
//                    <|assistant|>
//                    """)));
//            System.out.println("decode 972: "+(tokenizerLoader.decode(tokens, 972)));
            for(long token:token_s){
                System.out.print(tokenizerLoader.decode(tokens, (int)token));
                System.out.flush();
            }
        }

    }

    @Test
    public void testTokenize() {
        try(Arena arena = Arena.ofShared()){
            var tokenizerLoader = new TokenizerLoader(arena);
            var ids = new TokenizerIDS(arena);
            tokensFramework.loadTokenizer(tokenizerLoader.getMemorySegment(),
                    "E:\\ZeroPlan\\Java\\JDnn-Zero\\JDnnZero\\jdz-test\\libconfig\\onnx\\glm_tokenzer.json"
            );

            Runnable task=()->{
                var start_time = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getName()+": "+start_time);
                tokensFramework.encode(
                        tokenizerLoader.getMemorySegment(), """
                                [gMASK]<sop><|system|>你是一个有用的AI助手。
                                <|user|>你好
                                <|assistant|>""", ids.getMemorySegment()
                );
                System.out.println(Arrays.toString(ids.ids.getForIntArrayArray(ids.len.get())));
                var tokens = new TokenizerTokens(arena);
                tokensFramework.decodeTokens(
                        tokenizerLoader.getMemorySegment(),
                        ids.getMemorySegment(),
                        tokens.getMemorySegment()
                );
                var end_time = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getName()+": "+end_time);
                System.out.println(Thread.currentThread().getName()+": "+tokens.tokens.getForString());
                System.out.println(Thread.currentThread().getName()+": "+(end_time-start_time));
            };

            var thread_1 = new Thread(task);
            var thread_2 = new Thread(task);
            thread_1.start();
            thread_2.start();

            thread_1.join();
            thread_2.join();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
