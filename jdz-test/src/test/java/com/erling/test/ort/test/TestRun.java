package com.erling.test.ort.test;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
import com.erling.tokens.framework.TokensFramework;
import com.erling.tokens.struct.TokenizerIDS;
import com.erling.tokens.struct.TokenizerLoader;
import com.erling.tokens.struct.TokenizerTokens;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestRun {
    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GeneralDnnLib_Zero_ort_core_framework",
            useMappingConfig = false
    )
    public OnnxRTExample onnxRTExample;

    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GDLZTokens",
            useMappingConfig = false
    )
    public TokensFramework tokensFramework;


    public TestRun(){
        SetRunTimeEnv.SET.run();
        DyLinkLibLoaderForFFM.load(this);
    }

    @Test
    public void loadRun() {
        try (Arena arena = Arena.ofShared()) {
            var tokenizerLoader = new TokenizerLoader(arena);
            var ids = new TokenizerIDS(arena);
//            var kv_list = new OnnxExamKv(arena);
            var token_str = new TokenizerTokens(arena);
            tokensFramework.loadTokenizer(tokenizerLoader.getMemorySegment(),
                    "libconfig/onnx/ds_r1_tokens.json"
            );

            tokensFramework.encode(
                    tokenizerLoader.getMemorySegment(), """
                           <｜User｜>你好啊,请介绍你自己
                           <｜Assistant｜>
                           """, ids.getMemorySegment()
            );
            var int_idsArray = ids.ids.getForIntArrayArray(ids.len.get());

            long[] long_idsArray = Arrays.stream(int_idsArray).mapToLong(Long::valueOf).toArray();
            var framework = onnxRTExample.onnxRTInf.CreateOnnxRtFramework(arena.allocateUtf8String(
                    "E:\\ZeroPlan\\Java\\JDnn-Zero\\JDnnZero\\jdz-test\\libconfig\\onnx\\core_json.json"
            ));

            OnnxRTCtxExample ctxExample = new OnnxRTCtxExample(arena);
            onnxRTExample.onnxRTInf.CreateCtx(framework, ctxExample.getMemorySegment());

            ExecutorService executor = Executors.newFixedThreadPool(3);
            for (int i = 0; i < 3; i++) {
                executor.submit(() -> {
                    long start = System.currentTimeMillis();
                    System.out.println(Thread.currentThread().getName() + " start: " + start);
                    int token_id = onnxRTExample.onnxRTInf.RunExam(
                            framework,
                            arena.allocateArray(ValueLayout.JAVA_LONG, long_idsArray),
                            ids.len.get(),
                            ctxExample.getMemorySegment()  // ⚠️ 共享同一 ctx，不安全
                    );
                    System.out.println(Thread.currentThread().getName() + " end: " + System.currentTimeMillis());
                });
            }
            executor.shutdown();
            try {
                executor.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    @Test
    public void loadTokenizer() {
        System.setProperty("file.encoding", "UTF-8");
        try (Arena arena = Arena.ofShared()) {
            var tokenizerLoader = new TokenizerLoader(arena);
            var ids = new TokenizerIDS(arena);
            tokensFramework.loadTokenizer(tokenizerLoader.getMemorySegment(),
                    "libconfig/onnx/ds_r1_tokens.json"
            );

            tokensFramework.encode(
                    tokenizerLoader.getMemorySegment(), """
                           <｜User｜>你好啊,请介绍你自己
                           <｜Assistant｜>
                            """, ids.getMemorySegment()
            );
            System.out.println(Arrays.toString(ids.ids.getForIntArrayArray(ids.len.get())));
            var tokens = new TokenizerTokens(arena);
//            int[] ts = {
//                    99692, 3837, 20002, 36587, 2073, 108386, 103924, 11,
//                    14880, 100157, 107828, 33590, 104544, 101219, 116657, 3837,
//                    87267, 101219, 116657, 100631, 99172, 81167, 103992, 1773,
//                    35946, 99730, 60726, 113369, 29490, 104493, 3837, 101889,
//                    100157, 99283, 3837, 87267, 100630, 97611, 101294, 5373,
//                    100565, 5373, 103059, 49567, 3837, 99654, 26232, 105344,
//                    100364, 20002, 8997, 151649, 271, 108386, 6313, 112169,
//                    104639, 56568, 1773, 104198, 104455, 110498, 3837, 67071,
//                    105538, 102217, 30918, 50984, 9909, 33464, 39350, 7552,
//                    73218, 100013, 1773, 35946, 107618, 67338, 107018, 33108,
//                    104315, 101042, 100364, 20002, 107124, 3837, 102056, 110117,
//                    86119, 57191, 85106, 100364, 3837, 102422, 73670, 106525,
//                    6313, 151643, 151646, 271, 99692, 3837, 35946, 108449,
//                    1773, 99212, 104139, 109944, 100364, 103929, 101037, 94432,
//                    151649, 271, 111308, 6313, 104198, 67071, 105538, 102217
//            };
            int[] ts = {
                    99692, 3837, 20002, 36587, 2073, 108386, 103924, 11, 14880, 100157,
                    107828, 33590, 104544, 101219, 116657, 3837, 87267, 101219, 116657, 100631,
                    99172, 81167, 103992, 1773, 35946, 99730, 60726, 113369, 29490, 104493,
                    3837, 101889, 100157, 99283, 3837, 87267, 100630, 97611, 101294, 5373,
                    100565, 5373, 103059, 49567, 3837, 99654, 26232, 105344, 100364, 20002,
                    8997, 151649, 271, 108386, 6313, 112169, 104639, 56568, 1773, 104198,
                    104455, 110498, 3837, 67071, 105538, 102217, 30918, 50984, 9909, 33464,
                    39350, 7552, 73218, 100013, 1773, 35946, 107618, 67338, 107018, 33108,
                    104315, 101042, 100364, 20002, 107124, 3837, 102056, 110117, 86119, 57191,
                    85106, 100364, 3837, 102422, 73670, 106525, 6313, 151643, 151646, 271,
                    99692, 3837, 35946, 108449, 1773, 99212, 104139, 109944, 100364, 103929,
                    101037, 94432, 151649, 271, 111308, 6313, 104198, 67071, 105538, 102217,
                    30918
            };
//            tokensFramework.decodeToken(
//                    tokenizerLoader.getMemorySegment(),
//                    99692,
//                    tokens.getMemorySegment()
//            );
//            var ret =  tokensFramework.decodeTokens(
//                    tokenizerLoader.getMemorySegment(),
//                    ids.getMemorySegment(),
//                    tokens.getMemorySegment()
//            );
//
            for (int t : ts) {
                tokensFramework.decodeToken(
                        tokenizerLoader.getMemorySegment(),
                        t,
                        tokens.getMemorySegment()
                );
                System.out.println(tokens.tokens.getForString());
                if(Objects.equals(tokens.tokens.getForString(), "<｜end▁of▁sentence｜>")){
                    System.out.println("eos id=="+t);
                }
            }

//            tokensFramework.decodeToken(
//                    tokenizerLoader.getMemorySegment(),
//                    3837,
//                    tokens.getMemorySegment()
//            );
//            System.out.println(tokens.tokens.getForString());
        }

    }
}
