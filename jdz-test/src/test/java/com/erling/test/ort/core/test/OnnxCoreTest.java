package com.erling.test.ort.core.test;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
import com.erling.core.load.ffm.api.cpp.hook.SetConfig;
import com.erling.ort.llm.framework.ffm.OnnxRtLlmFramework;
import com.erling.ort.llm.struct.*;
import com.erling.tokens.framework.TokensFramework;
import com.erling.tokens.struct.TokenizerIDS;
import com.erling.tokens.struct.TokenizerLoader;
import com.erling.tokens.struct.TokenizerTokens;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.Arrays;

public class OnnxCoreTest {




    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GeneralDnnLib_Zero_ort_llm",
            useMappingConfig = false
    )
    @SetConfig(
            args = {
                    "./libconfig/onnx/core_json.json"
            }
    )
    public OnnxRtLlmFramework onnxRtLlmFramework;

    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GDLZTokens",
            useMappingConfig = false
    )
    @SetConfig(
            args = {
                    "E:\\ZeroPlan\\Java\\JDnn-Zero\\JDnnZero\\jdz-test\\libconfig\\onnx\\ds_r1_tokens.json"
            }
    )
    public TokensFramework tokensFramework;

    public OnnxCoreTest(){
        SetRunTimeEnv.SET.run();
        DyLinkLibLoaderForFFM.load(this);
    }

    @Test
    public void Auto(){

    }

    @Test
    public void load(){
        try(Arena arena = Arena.ofShared()){
            MemorySegment framework = onnxRtLlmFramework.onnxRtLlmInf.CreateLLmFramework(
                    arena.allocateUtf8String("./libconfig/onnx/core_json.json")
            );

        }
    }



    @Test
    public void loadOnnx() throws IOException {
        try(Arena arena = Arena.ofShared()){
            var kv=  new OnnxRtLlmKv(arena);
            var ctx= new OnnxRtLlmCtx(arena);
            var param = new OnnxRtLlmParam(arena);

            ctx.batch.set(1);
            param.temperature.set(0.0f);

            MemorySegment framework = onnxRtLlmFramework.onnxRtLlmInf.CreateLLmFramework(
                    arena.allocateUtf8String("./libconfig/onnx/core_json.json")
            );
            var tokenizerLoader = new TokenizerLoader(arena);
            var ids = new TokenizerIDS(arena);
            var token = new TokenizerTokens(arena);
            tokensFramework.loadTokenizer(tokenizerLoader.getMemorySegment(),
                    "E:\\ZeroPlan\\Java\\JDnn-Zero\\JDnnZero\\jdz-test\\libconfig\\onnx\\ds_r1_tokens.json"
            );

            tokensFramework.encode(
                    tokenizerLoader.getMemorySegment(), """
                            <｜User｜>
                            你好,请介绍你自己
                            <｜Assistant｜>
                            """, ids.getMemorySegment()
            );
            var longArray = Arrays.stream
                    (ids.ids.getForIntArrayArray(ids.len.get()))
                    .mapToLong(Long::valueOf).toArray();

            onnxRtLlmFramework.onnxRtLlmInf.InitSampler(param.getMemorySegment(),ctx.getMemorySegment());

            int ret = onnxRtLlmFramework.onnxRtLlmInf.InitBatchForTokenIds(
                    framework,
                    ctx.getMemorySegment(),kv.getMemorySegment(),
                    arena.allocateArray(ValueLayout.JAVA_LONG,longArray),longArray.length);
            if(ret==-1001){
                throw new RuntimeException("InitBatchForTokenIds failed");
            }
            tokensFramework.decodeToken(
                    tokenizerLoader.getMemorySegment(), Math.toIntExact(ctx.next_token_id.get()),
                    token.getMemorySegment());
            System.out.print(token.tokens.getForString());
            System.out.flush();
            while (ctx.next_token_id.get()!=151643){
                onnxRtLlmFramework.onnxRtLlmInf.GenerateToken(
                        framework,
                        ctx.getMemorySegment(),
                        kv.getMemorySegment()
                );

                tokensFramework.decodeToken(
                        tokenizerLoader.getMemorySegment(), Math.toIntExact(ctx.next_token_id.get()),
                        token.getMemorySegment());

                System.out.print(token.tokens.getForString());
                System.out.flush();
            }
            System.out.println();
            onnxRtLlmFramework.onnxRtLlmInf.GetCtxInfo(ctx.getMemorySegment());

            tokensFramework.encode(
                    tokenizerLoader.getMemorySegment(), """
                            <｜User｜>
                            还记得我们讨论了什么内容吗
                            <｜Assistant｜>
                            """, ids.getMemorySegment()
            );

            var longArray2 = Arrays.stream
                            (ids.ids.getForIntArrayArray(ids.len.get()))
                    .mapToLong(Long::valueOf).toArray();

            onnxRtLlmFramework.onnxRtLlmInf.InitBatchForTokenIds(
                    framework,
                    ctx.getMemorySegment(),kv.getMemorySegment(),
                    arena.allocateArray(ValueLayout.JAVA_LONG,longArray2),longArray2.length);

            tokensFramework.decodeToken(
                    tokenizerLoader.getMemorySegment(), Math.toIntExact(ctx.next_token_id.get()),
                    token.getMemorySegment());
            System.out.print(token.tokens.getForString());
            System.out.flush();

            while (ctx.next_token_id.get()!=151643){
                onnxRtLlmFramework.onnxRtLlmInf.GenerateToken(
                        framework,
                        ctx.getMemorySegment(),
                        kv.getMemorySegment()
                );

                tokensFramework.decodeToken(
                        tokenizerLoader.getMemorySegment(), Math.toIntExact(ctx.next_token_id.get()),
                        token.getMemorySegment());

                System.out.print(token.tokens.getForString());
                System.out.flush();
            }

            System.out.println();
            onnxRtLlmFramework.onnxRtLlmInf.GetCtxInfo(ctx.getMemorySegment());
        }

    }

    public long[] encode_for_str(Arena arena, String str,TokenizerLoader tokenizerLoader){
        var ids = new TokenizerIDS(arena);

        tokensFramework.encode(
                tokenizerLoader.getMemorySegment(), str, ids.getMemorySegment()
        );
        return Arrays.stream
                            (ids.ids.getForIntArrayArray(ids.len.get()))
                    .mapToLong(Long::valueOf).toArray();
    }

    public String decode_for_str(Arena arena, long token_ids,TokenizerLoader tokenizerLoader){
        var token = new TokenizerTokens(arena);
        tokensFramework.decodeToken(
                tokenizerLoader.getMemorySegment(), Math.toIntExact(token_ids),
                token.getMemorySegment());
        return token.tokens.getForString();
    }



    @Test
    public void decodeTest(){
        try(Arena arena = Arena.ofShared()) {
            var tokenizerLoader = new TokenizerLoader(arena);
            tokensFramework.loadTokenizer(tokenizerLoader.getMemorySegment()
//                    ,"E:\\ZeroPlan\\Java\\JDnn-Zero\\JDnnZero\\jdz-test\\libconfig\\onnx\\ds_r1_tokens.json"
            );
            System.out.println(decode_for_str(arena,0,tokenizerLoader));
        }

    }





}
