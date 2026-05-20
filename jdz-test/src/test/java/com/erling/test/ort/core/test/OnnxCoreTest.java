package com.erling.test.ort.core.test;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
import com.erling.core.load.ffm.api.cpp.hook.SetConfig;
import com.erling.ort.core.framework.ffm.OnnxCoreFramework;
import com.erling.ort.core.shape.OnnxMirrorShape;
import com.erling.ort.core.struct.OnnxRtInput;
import com.erling.ort.core.type.OnnxDataType;
import com.erling.ort.llm.framework.ffm.OnnxRtLlmFramework;
import com.erling.ort.llm.model.OnnxLLmModel;
import com.erling.ort.llm.model.rt.RtSnapshot;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.erling.ort.core.struct.OnnxRtShape;
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
            name = "GeneralDnnLib_Zero_ort_core",
            useMappingConfig = false
    )

    public OnnxCoreFramework onnxCoreFramework;
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
    public void getSize(){
        try(Arena arena = Arena.ofShared()){
            var shape = new OnnxRtShape(arena);

//            shape.
//                    shape("input",new long[]{1, 2, 0, 128}).
//                    data(new long[]{1000,2000,3000,4000});
            shape.shape(new long[]{1,2}).data(new short[]{1,2});
            shape.type(OnnxDataType.FLOAT16.i());
//            shape.data_type.set(OnnxDataType.INT64.i());
            onnxRtLlmFramework.onnxRtLlmInf.GetShapeInfo(shape.getMemorySegment());

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
                            能写一段简单的python吗，比如打印hello world
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
    public void build_to_run(){
        try(Arena arena = Arena.ofShared()){
            var kv=  new OnnxRtLlmKv(arena);
            var ctx= new OnnxRtLlmCtx(arena);
            var param = new OnnxRtLlmParam(arena);

            param.temperature.set(0.5f);

            var tokenizerLoader = new TokenizerLoader(arena);
            tokensFramework.loadTokenizer(tokenizerLoader.getMemorySegment(),
                    "E:\\ZeroPlan\\Java\\JDnn-Zero\\JDnnZero\\jdz-test\\libconfig\\onnx\\ds_r1_tokens.json"
            );

            long[] token_ids = encode_for_str(arena,
                        """
                            <｜User｜>
                            能写一段简单的python吗，比如打印hello world
                            <｜Assistant｜>
                            """,tokenizerLoader
            );
            long[] attention_ids= new long[token_ids.length];
            long[] positions    = new long[token_ids.length];

            Arrays.fill(attention_ids,1L);
            if(ctx.token_pos.get()!=0){
                 for (int i=0;i<positions.length;++i){
                    positions[i]=ctx.token_pos.get()+i;
                 }
            }else{
                for(int i=0;i<positions.length;++i){
                    positions[i]=i;
                }
            }
            ctx.token_pos.set(ctx.token_pos.get() + token_ids.length);
            MemorySegment framework = onnxRtLlmFramework.onnxRtLlmInf.CreateLLmFramework(
                    arena.allocateUtf8String("./libconfig/onnx/core_json.json")
            );
//            onnxRtLlmFramework.onnxRtLlmInf.GetLLmEngineInfo(framework);

            var input = new OnnxRtInput(arena);
            var input_shape = new OnnxRtShape(arena).
                    shape(new long[]{1,token_ids.length}).
                    data(token_ids).
                    type(OnnxDataType.INT64.i());

            var attention_shape = new OnnxRtShape(arena).
                    shape(new long[]{1,token_ids.length}).
                    data(attention_ids).
                    type(OnnxDataType.INT64.i());

            var positions_shape = new OnnxRtShape(arena).
                    shape(new long[]{1,token_ids.length}).
                    data(positions).
                    type(OnnxDataType.INT64.i());


            var kv_shape = new OnnxRtShape(arena).
                                shape(new long[]{1,2,0,128}).
                                type(OnnxDataType.FLOAT16.i());

            int ret = onnxRtLlmFramework.onnxRtLlmInf.SetInput(
                    framework,
                    input_shape.getMemorySegment(),
                    input.getMemorySegment());
            System.out.println("SetInput ret for set inpu_ids:"+ret);

            ret = onnxRtLlmFramework.onnxRtLlmInf.SetInput(
                    framework,
                    attention_shape.getMemorySegment(),
                    input.getMemorySegment());
            System.out.println("SetInput ret for set attention_ids:"+ret);

            ret = onnxRtLlmFramework.onnxRtLlmInf.SetInput(
                    framework,
                    positions_shape.getMemorySegment(),
                    input.getMemorySegment());
            System.out.println("SetInput ret for set positions:"+ret);

            for(int i=0;i<56;i++){
                ret = onnxRtLlmFramework.onnxRtLlmInf.SetInput(
                        framework,
                        kv_shape.getMemorySegment(),
                        input.getMemorySegment());

                if(ret==-1001){
                    throw new RuntimeException("SetInput failed");
                }
                System.out.println("SetInput ret:"+ret);
            }

            onnxRtLlmFramework.onnxRtLlmInf.InitSampler(param.getMemorySegment(),ctx.getMemorySegment());

            ret = onnxRtLlmFramework.onnxRtLlmInf.Prefill(
                    framework,
                    ctx.getMemorySegment(),
                    kv.getMemorySegment(),
                    input.getMemorySegment()
            );

            System.out.println("Prefill ret:"+ret);
            System.out.println("next token_id:"+ctx.next_token_id.get());
            System.out.println("next token:" +decode_for_str(arena,ctx.next_token_id.get(),tokenizerLoader));

            while (ctx.next_token_id.get()!=151643){
                var startTime = System.currentTimeMillis();
                long[] next_token_ids = {ctx.next_token_id.get()};
                long[] next_attention_ids= new long[]{1L};
                long[] next_positions    = new long[]{ctx.token_pos.get()};

                 var next_token_ids_shape = new OnnxRtShape(arena).
                    shape(new long[]{1,1}).
                    data(next_token_ids).
                    type(OnnxDataType.INT64.i());

                 var next_attention_ids_shape = new OnnxRtShape(arena).
                    shape(new long[]{1,1}).
                    data(next_attention_ids).
                    type(OnnxDataType.INT64.i());

                 var next_positions_shape = new OnnxRtShape(arena).
                    shape(new long[]{1,1}).
                    data(next_positions).
                    type(OnnxDataType.INT64.i());

                var next_input = new OnnxRtInput(arena);

                 ret = onnxRtLlmFramework.onnxRtLlmInf.SetInput(
                        framework,
                        next_token_ids_shape.getMemorySegment(),
                        next_input.getMemorySegment());

                ret = onnxRtLlmFramework.onnxRtLlmInf.SetInput(
                        framework,
                        next_attention_ids_shape.getMemorySegment(),
                        next_input.getMemorySegment());


                ret = onnxRtLlmFramework.onnxRtLlmInf.SetInput(
                        framework,
                        next_positions_shape.getMemorySegment(),
                        next_input.getMemorySegment());


                ret = onnxRtLlmFramework.onnxRtLlmInf.Decode(
                        framework,
                        ctx.getMemorySegment(),
                        kv.getMemorySegment(),
                        next_input.getMemorySegment()
                );
//                System.out.println("Decode ret:"+ret+" cost time:"+(System.currentTimeMillis()-startTime));

                System.out.print(decode_for_str(arena,ctx.next_token_id.get(),tokenizerLoader));
//                System.out.println("next token_id:"+ctx.next_token_id.get());
                System.out.flush();
            }

        }
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

    @Test
    public void modelTest(){
        OnnxLLmModel model = new OnnxLLmModel().
            setOnnxRtLlmFramework(onnxRtLlmFramework).
            setTokenizer(tokensFramework);
        try(Arena arena = Arena.ofShared()){

            String prompt = """
                            <｜User｜>
                            能写一段简单的python吗，比如打印hello world
                            <｜Assistant｜>
                            """;



            Runnable chat = ()->{

                    List<OnnxMirrorShape<?>> inputShapes = new ArrayList<>();
                    var input= new OnnxRtInput(arena);
                    var ctx = new OnnxRtLlmCtx(arena);
                    var kv =  new OnnxRtLlmKv(arena);
                    model.
                    setSampler(() -> ctx, () -> new OnnxRtLlmParam(arena)).
                    buildInputShape(
                            prompt,
                            ctx,
                            inputShapes
                    ).
                    buildKvShape(() -> {
                                List<OnnxMirrorShape<?>> list = new ArrayList<>();
                                var kv_mirror = new OnnxMirrorShape<>(
                                        new long[]{1, 2, 0, 128},
                                        "AUTO",
                                        OnnxDataType.FLOAT16,
                                        null
                                );
                                for (int i = 0; i < 2 * 28; i++) {
                                    list.add(kv_mirror);
                                }
                                return list;
                            },
                            inputShapes
                    ).
                    buildAllInputShape(
                            input,
                            inputShapes,
                            arena
                    ).chat(() -> ctx, () -> kv, () -> input, arena, 151643,
                            (token_str, token_count) -> {
                                System.out.print(token_str);
                                System.out.flush();
                                return true;
                            }
                    );
            };

            var thread1 = new Thread(chat);
            var thread2 = new Thread(chat);

            thread1.start();
            thread2.start();

            thread1.join();
            thread2.join();
//            model.prefill(ctx,kv,input);
//            System.out.println("next token_id:"+ctx.next_token_id.get());
//            model.decode(ctx,kv,input,arena);
//            System.out.println("next token_id:"+ctx.next_token_id.get());

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }



}
