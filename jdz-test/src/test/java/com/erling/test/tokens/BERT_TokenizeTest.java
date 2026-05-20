package com.erling.test.tokens;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
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
    public TokensFramework tokensFramework;

    public BERT_TokenizeTest(){

        SetRunTimeEnv.SET.run();
        DyLinkLibLoaderForFFM.load(this);
    }

    @Test
    public void testTokenize() {
        try(Arena arena = Arena.ofShared()){
            var tokenizerLoader = new TokenizerLoader(arena);
            var ids = new TokenizerIDS(arena);
            tokensFramework.loadTokenizer(tokenizerLoader.getMemorySegment(),
                    "E:\\ZeroPlan\\Java\\JDnn-Zero\\JDnnZero\\jdz-test\\libconfig\\trt_rag_config\\tokenizer.json"
            );

            Runnable task=()->{
                var start_time = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getName()+": "+start_time);
                tokensFramework.encode(
                        tokenizerLoader.getMemorySegment(), "你好,请介绍你自己,hello world", ids.getMemorySegment()
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
