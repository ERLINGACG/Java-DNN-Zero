package com.erling.test.trt.rag;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
import com.erling.core.load.ffm.api.cpp.hook.SetConfig;
import com.erling.trt.rag.framework.ffm.TrtEmbFrameFFM;
import com.erling.trt.rag.model.data.FillMode;

import com.erling.trt.rag.model.data.InputDims;
import com.erling.trt.rag.model.data.OutPutDims;
import com.erling.trt.rag.model.TrtEmbModel;
import com.erling.trt.rag.struct.TrtEmbCtx;
import com.erling.trt.rag.struct.TrtEmbData;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;

public class TrtEmbRagTest {
    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GeneralDnnLib_Zero_trt_emb",
            useMappingConfig = false
    )
    @SetConfig(args = {"libconfig/trt_rag_config/bge_config.json"})

    public TrtEmbFrameFFM trtEmbFrameFFM;

    public TrtEmbRagTest() {
        SetRunTimeEnv.SET.run();
        DyLinkLibLoaderForFFM.load(this);
    }
    @Test
    public void load(){
    }

    @Test
    public void model_test(){
        long[] inputValues = { 31380, 55951, 15120, 87502, 50338, 69962, 25287, 5486,
                45736, 53434, 31968, 39607, 73981, 78244, 1811};
        try(Arena arena = Arena.ofConfined()) {
            var ctx = new TrtEmbCtx(arena);
            var model = new TrtEmbModel().
                    setFrameFFM(trtEmbFrameFFM).
                    creatCtx(ctx).
                    setDims(arena,()->new InputDims(
                            ctx,
                            "input_ids",
                            new int[]{1, inputValues.length}
                            )
                    ).setDims(arena,()->new InputDims(
                            ctx,
                            "token_type_ids",
                            new int[]{1, inputValues.length}
                            )
                    ).setDims(arena,()->new InputDims(
                            ctx,
                            "attention_mask",
                            new int[]{1, inputValues.length}
                            )
                    ).getDims(arena,()->new OutPutDims(
                            ctx,
                            "output"
                            )
                    ).getDims(arena,()->new OutPutDims(
                            ctx,
                            "677"
                            )
                    ).postCudaInt64(
                            ctx, arena,
                            "input_ids", inputValues,
                            FillMode.NORMAL
                    ).postCudaInt64(
                            ctx, arena,
                            "token_type_ids", inputValues,
                            FillMode.ALL_ZERO
                    ).postCudaInt64(
                            ctx, arena,
                            "attention_mask", inputValues,
                            FillMode.ALL_ONE
                    ).forward(ctx).
                    getEmbedding(ctx,arena,new TrtEmbData(arena),"677");
            }
    }
    @Test
    public void model_test_2(){
        long[] inputValues = { 31380, 55951, 15120, 87502, 50338, 69962, 25287, 5486,
                45736, 53434, 31968, 39607, 73981, 78244, 1811};
        try(Arena arena = Arena.ofConfined()) {
            var ctx = new TrtEmbCtx(arena);
            var embData = new TrtEmbData(arena);
            var model = new TrtEmbModel().
                    setFrameFFM(trtEmbFrameFFM).
                    creatCtx(ctx).
                    setDims(arena,()->new InputDims(
                                    ctx,
                                    "input_ids",
                                    new int[]{1, inputValues.length}
                            )
                    ).setDims(arena,()->new InputDims(
                                    ctx,
                                    "attention_mask",
                                    new int[]{1, inputValues.length}
                            )
                    ).getDims(arena,()->new OutPutDims(
                                    ctx,
                                    "last_hidden_state"
                            )
                    ).postCudaInt64(
                            ctx, arena,
                            "input_ids", inputValues,
                            FillMode.NORMAL
                    ).postCudaInt64(
                            ctx, arena,
                            "attention_mask", inputValues,
                            FillMode.ALL_ZERO
                    ).forward(ctx);

                    model.getEmbedding(ctx,arena,embData,"last_hidden_state");
                    System.out.println(embData.embedding_size.get());
                    var floatData = embData.embedding.getForFloatArray(embData.embedding_size.get());
                    for(int i = 0;i<30;i++){
                        System.out.println(floatData[i]);
                    }
        }
    }


//    @Test
//    public void forward_1(){
//        int[] inputValues = { 31380, 55951, 15120, 87502, 50338, 69962, 25287, 5486,
//                45736, 53434, 31968, 39607, 73981, 78244, 1811};
//
//        trtEmbFrameFFM.frameInf.GetEngineInfo(trtEmbFrameFFM.modelSegment());
//        try (Arena arena = Arena.ofConfined()){
//            var embCtx = new TrtEmbCtx(arena);
//            var embData = new TrtEmbData(arena);
//            trtEmbFrameFFM.frameInf.CreateCtx(trtEmbFrameFFM.modelSegment(),embCtx.getMemorySegment());
//
//            trtEmbFrameFFM.frameInf.SetDims(embCtx.getMemorySegment(),
//                    arena.allocateUtf8String("input_ids"),
//                    arena.allocateArray(ValueLayout.JAVA_INT, 1,inputValues.length),2);
//
//            trtEmbFrameFFM.frameInf.SetDims(embCtx.getMemorySegment(),
//                    arena.allocateUtf8String("token_type_ids"),
//                    arena.allocateArray(ValueLayout.JAVA_INT, 1,inputValues.length),2);
//
//            trtEmbFrameFFM.frameInf.SetDims(embCtx.getMemorySegment(),
//                    arena.allocateUtf8String("attention_mask"),
//                    arena.allocateArray(ValueLayout.JAVA_INT, 1,inputValues.length),2);
////
//            trtEmbFrameFFM.frameInf.GetDims(
//                    trtEmbFrameFFM.modelSegment(),
//                    embCtx.getMemorySegment(),
//                    arena.allocateUtf8String("output")
//            );
//
//            trtEmbFrameFFM.frameInf.GetDims(
//                    trtEmbFrameFFM.modelSegment(),
//                    embCtx.getMemorySegment(),
//                    arena.allocateUtf8String("677")
//            );
//
//            trtEmbFrameFFM.frameInf.PostCuda(embCtx.getMemorySegment(),
//                    arena.allocateUtf8String("input_ids"),
//                    arena.allocateArray(ValueLayout.JAVA_INT, inputValues),inputValues.length,0);
////
//            trtEmbFrameFFM.frameInf.PostCuda(embCtx.getMemorySegment(),
//                    arena.allocateUtf8String("token_type_ids"),
//                    arena.allocateArray(ValueLayout.JAVA_INT, inputValues.length),inputValues.length,2);
////
//            trtEmbFrameFFM.frameInf.PostCuda(embCtx.getMemorySegment(),
//                    arena.allocateUtf8String("attention_mask"),
//                    arena.allocateArray(ValueLayout.JAVA_INT, inputValues.length),inputValues.length,1);
//
//            trtEmbFrameFFM.frameInf.Forward(trtEmbFrameFFM.modelSegment(),
//                    embCtx.getMemorySegment());
////
//            trtEmbFrameFFM.frameInf.GetEmbedding(
//                    embCtx.getMemorySegment(),embData.getMemorySegment(),
//                    arena.allocateUtf8String("677"),
//                    512,
//                    Float.SIZE
//            );
//            System.out.println("============get output========");
//
//            trtEmbFrameFFM.frameInf.GetEmbedding(
//                    embCtx.getMemorySegment(),embData.getMemorySegment(),
//                    arena.allocateUtf8String("output"),
//                    7680,
//                    Float.SIZE
//            );
//
//
//
//        }
//
//    }
//
//



//    @Test
//    public void forward() {
//        int[] inputValues = {31380, 55951, 15120, 87502, 50338, 69962, 25287, 5486,
//                45736, 53434, 31968, 39607, 73981, 78244, 1811};
//
//        trtEmbFrameFFM.frameInf.GetEngineInfo(trtEmbFrameFFM.modelSegment());
//        try (Arena arena = Arena.ofConfined()) {
//            var embCtx = new TrtEmbCtx(arena);
//            var embData = new TrtEmbData(arena);
//            trtEmbFrameFFM.frameInf.CreateCtx(trtEmbFrameFFM.modelSegment(), embCtx.getMemorySegment());
//
//            trtEmbFrameFFM.frameInf.SetDims(embCtx.getMemorySegment(),
//                    arena.allocateUtf8String("input_ids"),
//                    arena.allocateArray(ValueLayout.JAVA_INT, 1, inputValues.length), 2);
//
//            trtEmbFrameFFM.frameInf.SetDims(embCtx.getMemorySegment(),
//                    arena.allocateUtf8String("attention_mask"),
//                    arena.allocateArray(ValueLayout.JAVA_INT, 1, inputValues.length), 2);
////
//            trtEmbFrameFFM.frameInf.GetDims(
//                    trtEmbFrameFFM.modelSegment(),
//                    embCtx.getMemorySegment(),
//                    arena.allocateUtf8String("last_hidden_state")
//            );
//
//
//            trtEmbFrameFFM.frameInf.PostCuda(embCtx.getMemorySegment(),
//                    arena.allocateUtf8String("input_ids"),
//                    arena.allocateArray(ValueLayout.JAVA_INT, inputValues), inputValues.length, 0);
//
//            trtEmbFrameFFM.frameInf.PostCuda(embCtx.getMemorySegment(),
//                    arena.allocateUtf8String("attention_mask"),
//                    arena.allocateArray(ValueLayout.JAVA_INT, inputValues.length), inputValues.length, 1);
//
//            trtEmbFrameFFM.frameInf.Forward(trtEmbFrameFFM.modelSegment(),
//                    embCtx.getMemorySegment());
////
//            trtEmbFrameFFM.frameInf.GetEmbedding(
//                    embCtx.getMemorySegment(), embData.getMemorySegment(),
//                    arena.allocateUtf8String("last_hidden_state"), 11520,
//                    Float.SIZE
//            );
//
//        }
//
//
//    }
}
