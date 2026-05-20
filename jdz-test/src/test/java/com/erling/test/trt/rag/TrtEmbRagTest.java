package com.erling.test.trt.rag;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
import com.erling.core.load.ffm.api.cpp.hook.SetConfig;
import com.erling.trt.rag.framework.ffm.TrtEmbFrameFFM;
import com.erling.trt.rag.model.RunKt;
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

//    @JdzFrameFFM(
//            rootPath = "./libconfig/share",
//            name = "GeneralDnnLib_Zero_trt_emb",
//            useMappingConfig = false
//    )
//    @SetConfig(args = {"libconfig/trt_rag_config/bge_config.json"})
//
//    public TrtEmbFrameFFM trtEmbFrameFFM2;

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
                    ).setDims(arena,()->new InputDims(
                                    ctx,
                                    "token_type_ids",
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
                            FillMode.ALL_ONE
                    ).postCudaInt64(
                            ctx, arena,
                            "token_type_ids", inputValues,
                            FillMode.ALL_ONE

                    ).forward(ctx);

                    model.getPooledEmbedding(
                            ctx,arena,embData,
                            "last_hidden_state",
                            "attention_mask"
                    );
                    System.out.println(embData.embedding_size.get());
                    var floatData = embData.embedding.getForFloatArray(embData.embedding_size.get());
                    for(int i = 0;i<30;i++){
                        System.out.println(floatData[i]);
                    }
                    model.clearBindings(ctx);
        }
    }

    @Test
    public void model_thread_test(){


        try(Arena arena=Arena.ofShared()){
            var trt_f_1= trtEmbFrameFFM.clone();
            var trt_f_2= trtEmbFrameFFM.clone();

//            var ctx_1 = new TrtEmbCtx(arena);
//            var ctx_2 = new TrtEmbCtx(arena);
            var ctx=new TrtEmbCtx(arena);

            var embData_1 = new TrtEmbData(arena);
            var embData_2 = new TrtEmbData(arena);

            var model_1 = new TrtEmbModel();
            var model_2 = new TrtEmbModel();
            var startTime = System.currentTimeMillis();
            model_1.
                    setFrameFFM(trtEmbFrameFFM).creatCtx(ctx)
                    ;

            model_2.
                    setFrameFFM(trtEmbFrameFFM)
                 ;


            Runnable task1Ays=()->{
                long[] inputValues_1 = { 50338, 69962, 25287, 5486,
                        45736, 53434, 31968, 39607, 73981, 78244, 1811
                };
                System.out.println("task1Ays start time :"+System.currentTimeMillis());
//                model_1.creatCtx(ctx_1);
//                System.out.println("task1Ays creatCtx time :"+System.currentTimeMillis());
                synchronized (ctx) {
                    model_1.setDims(arena, () -> new InputDims(
                                    ctx,
                                    "input_ids",
                                    new int[]{1, inputValues_1.length}
                            )
                    ).setDims(arena, () -> new InputDims(
                                    ctx,
                                    "attention_mask",
                                    new int[]{1, inputValues_1.length}
                            )
                    ).setDims(arena, () -> new InputDims(
                                    ctx,
                                    "token_type_ids",
                                    new int[]{1, inputValues_1.length}
                            )
                    ).getDims(arena, () -> new OutPutDims(
                                    ctx,
                                    "last_hidden_state"
                            )
                    );
                    model_1.postCudaInt64(
                            ctx, arena,
                            "input_ids", inputValues_1,
                            FillMode.NORMAL
                    ).postCudaInt64(
                            ctx, arena,
                            "attention_mask", inputValues_1,
                            FillMode.ALL_ONE
                    ).postCudaInt64(
                            ctx, arena,
                            "token_type_ids", inputValues_1,
                            FillMode.ALL_ONE

                    );
                    System.out.println("task1Ays start forward time :" + System.currentTimeMillis());
                    model_1.synchronize(ctx);
                    model_1.forwardAsync(ctx);
                    System.out.println("task1Ays end time :" + System.currentTimeMillis());
                    model_1.getPooledEmbedding(
                            ctx, arena, embData_1,
                            "last_hidden_state",
                            "attention_mask"
                    );
                    var floatData = embData_1.embedding.getForFloatArray(embData_1.embedding_size.get());
                    for (int i = 0; i < 30; i++) {
                        System.out.println("thread 1:" + floatData[i]);
                    }

                }

            };
            Runnable task2Ays=()->{
                long[] inputValues_2 = { 31380, 55951, 15120, 87502, 50338, 69962, 25287, 5486,
                        45736, 53434, 31968, 39607, 73981, 78244
                };
                System.out.println("task2Ays start time :"+System.currentTimeMillis());
//                model_2.creatCtx(ctx_2);
//                System.out.println("task2Ays creatCtx time :"+System.currentTimeMillis());
                synchronized (ctx){
                    model_2.setDims(arena,()->new InputDims(
                                    ctx,
                                    "input_ids",
                                    new int[]{1, inputValues_2.length}
                            )
                    ).setDims(arena,()->new InputDims(
                                    ctx,
                                    "attention_mask",
                                    new int[]{1, inputValues_2.length}
                            )
                    ).setDims(arena,()->new InputDims(
                                    ctx,
                                    "token_type_ids",
                                    new int[]{1, inputValues_2.length}
                            )
                    ).getDims(arena,()->new OutPutDims(
                                    ctx,
                                    "last_hidden_state"
                            )
                    );
                    model_2.postCudaInt64(
                                    ctx, arena,
                                    "input_ids", inputValues_2,
                                    FillMode.NORMAL
                            ).postCudaInt64(
                                    ctx, arena,
                                    "attention_mask", inputValues_2,
                                    FillMode.ALL_ONE
                            ).postCudaInt64(
                                    ctx, arena,
                                    "token_type_ids", inputValues_2,
                                    FillMode.ALL_ONE

                            );
                            System.out.println("task2Ays start forward time :"+System.currentTimeMillis());
                            model_2.forwardAsync(ctx);
                            System.out.println("task2Ays end time :"+System.currentTimeMillis());
                            model_2.getPooledEmbedding(
                                    ctx,arena,embData_2,
                                    "last_hidden_state",
                                    "attention_mask"
                            );

                            var floatData = embData_2.embedding.getForFloatArray(embData_2.embedding_size.get());
                            for(int i = 0;i<30;i++){
                                System.out.println("thread 2:"+floatData[i]);
                            }
                }
            };



            var thread1Ays = new Thread(task1Ays);
            var thread2Ays = new Thread(task2Ays);


            thread1Ays.start();
            thread2Ays.start();



            thread2Ays.join();
            thread1Ays.join();
            var endTime = System.currentTimeMillis();
            System.out.println("total time :"+(endTime-startTime));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    public void forwardAsync_1() {
        long[] inputValues = { 31380, 55951, 15120, 87502, 50338, 69962, 25287, 5486,
                45736, 53434, 31968, 39607, 73981, 78244, 1811};
        try(Arena arena = Arena.ofConfined()) {
            var ctx = new TrtEmbCtx(arena);
            var embData = new TrtEmbData(arena);
            var startTime = System.currentTimeMillis();
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
                    ).setDims(arena,()->new InputDims(
                                    ctx,
                                    "token_type_ids",
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
                            FillMode.ALL_ONE
                    ).postCudaInt64(
                            ctx, arena,
                            "token_type_ids", inputValues,
                            FillMode.ALL_ONE

                    );

//            model.synchronize(ctx)

            model.forwardAsync(ctx);
            var endTime = System.currentTimeMillis();
            System.out.println("forwardAsync time: " + (endTime - startTime));
            model.getPooledEmbedding(
                    ctx,arena,embData,
                    "last_hidden_state",
                    "attention_mask"
            );

            var floatData = embData.embedding.getForFloatArray(embData.embedding_size.get());
            for(int i = 0;i<30;i++){
                System.out.println("thread 2:"+floatData[i]);
            }
            model.clearBindings(ctx);
        }
    }

    @Test
    public void forwardAsync_2() {
        long[] inputValues = { 31380, 55951, 15120, 87502, 50338, 69962, 25287, 5486,
                45736, 53434, 31968, 39607, 73981, 78244, 1811};
        try(Arena arena = Arena.ofShared()) {
            var ctx = new TrtEmbCtx(arena);
            var embData = new TrtEmbData(arena);
            var startTime = System.currentTimeMillis();
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
                    ).setDims(arena,()->new InputDims(
                                    ctx,
                                    "token_type_ids",
                                    new int[]{1, inputValues.length}
                            )
                    ).getDims(arena,()->new OutPutDims(
                                    ctx,
                                    "last_hidden_state"
                            )
                    );

            Runnable task_1=()->{
                var startTime_ = System.currentTimeMillis();
                model.postCudaInt64(
                        ctx, arena,
                        "input_ids", inputValues,
                        FillMode.NORMAL
                );
                var endTime = System.currentTimeMillis();
                System.out.println("postCudaInt64 1 time s:" + startTime_+" e:"+endTime);

            };

            Runnable task_2=()->{
                var startTime_ = System.currentTimeMillis();
                model.postCudaInt64(
                        ctx, arena,
                        "token_type_ids", inputValues,
                        FillMode.ALL_ONE
                );
                var endTime = System.currentTimeMillis();
                System.out.println("postCudaInt64 2 time s:" + startTime_+" e:"+endTime);

            };

            Runnable task_3=()->{
                var startTime_ = System.currentTimeMillis();
                model.postCudaInt64(
                        ctx, arena,
                        "attention_mask", inputValues,
                        FillMode.ALL_ONE
                );
                var endTime = System.currentTimeMillis();
                System.out.println("postCudaInt64 3 time s:" + startTime_+" e:"+endTime);
            };

            Runnable task_4=()->{
                model.forwardAsync(ctx);

            };

            var thread_1 = new Thread(task_1);
            var thread_2 = new Thread(task_2);
            var thread_3 = new Thread(task_3);

            var thread_4 = new Thread(task_4);


            thread_1.start();
            thread_2.start();
            thread_3.start();
            thread_4.start();

            try {
                thread_1.join();
                thread_2.join();
                thread_3.join();



                thread_4.join();

            } catch (InterruptedException e) {
                System.out.println("join thread error:"+e.getMessage());
            }

            var endTime = System.currentTimeMillis();
            System.out.println("forwardAsync time: " + (endTime - startTime));
            model.getPooledEmbedding(
                    ctx,arena,embData,
                    "last_hidden_state",
                    "attention_mask"
            );

            var floatData = embData.embedding.getForFloatArray(embData.embedding_size.get());
            for(int i = 0;i<30;i++){
                System.out.println("thread 2:"+floatData[i]);
            }
            model.clearBindings(ctx);
        }
    }

}
