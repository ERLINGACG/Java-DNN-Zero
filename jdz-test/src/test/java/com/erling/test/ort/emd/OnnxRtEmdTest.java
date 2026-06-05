package com.erling.test.ort.emd;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
import com.erling.core.load.ffm.api.cpp.hook.SetConfig;
import com.erling.ort.core.struct.OnnxRtShape;
import com.erling.ort.core.type.OnnxDataType;
import com.erling.ort.emd.framework.ffm.OnnxRtEmdFramework;
import com.erling.ort.emd.model.OnnxRtEmdModel;
import com.erling.ort.emd.struct.OnnxRtEmdCtx;
import com.erling.ort.emd.struct.OnnxRtEmdData;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OnnxRtEmdTest {
    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GeneralDnnLib_Zero_ort_emd",
            useMappingConfig = false
    )
    @SetConfig(
            args = {"./libconfig/onnx/emd/miniLM.json"}
    )
    private OnnxRtEmdFramework emdFramework;

    public OnnxRtEmdTest(){
        SetRunTimeEnv.SET.run();
        DyLinkLibLoaderForFFM.load(this);

    }

    @Test
    public void load(){
        assertNotNull(emdFramework, "emdFramework should have been injected by FFM");
    }

    @Test
    public void loadToConfig(){
        try(Arena arena = Arena.ofShared()){
            MemorySegment prt = emdFramework.emdInf.CreateEmdFramework(
                    arena.allocateUtf8String("./libconfig/onnx/emd/miniLM.json")
            );
            emdFramework.emdInf.GetEmdFrameworkEngineInfo(prt);
            emdFramework.emdInf.DestroyEmdFramework(prt);
        }

    }

    @Test
    public void test_run(){
        var input=new long[]{1,2,3,4};
        try(Arena arena = Arena.ofShared()){

            var ctx=new OnnxRtEmdCtx(arena);
            MemorySegment prt = emdFramework.emdInf.CreateEmdFramework(
                    arena.allocateUtf8String("./libconfig/onnx/emd/miniLM.json")
            );
            var shape_list=new ArrayList<OnnxRtShape>();
            var emd_data=new OnnxRtEmdData(arena);

            var input_ids=new OnnxRtShape(arena).
                    setName("input_ids").
                    setData(input).
                    setShape(new long[]{1,input.length}).
                    input();

            var attention_mask=new OnnxRtShape(arena).
                    setName("attention_mask").
                    setData(new long[]{1,1,1,1}).
                    setShape(new long[]{1,input.length}).
                    input();

            var token_type_ids=new OnnxRtShape(arena).
                    setName("token_type_ids").
                    setData(new long[]{0,0,0,0}).
                    setShape(new long[]{1,input.length}).
                    input();

            var last_hidden_state=new OnnxRtShape(arena).
                    setName("last_hidden_state").
                    setData(new float[]{0}).
                    setShape(new long[]{1,input.length,384}).
                    output();

            shape_list.add(input_ids);
            shape_list.add(attention_mask);
            shape_list.add(token_type_ids);
            shape_list.add(last_hidden_state);

            for(var shape:shape_list){
                emdFramework.emdInf.SetShape(shape.getMemorySegment(), ctx.getMemorySegment());
            }
            emdFramework.emdInf.Forward(prt, ctx.getMemorySegment(), emd_data.getMemorySegment());
            System.out.println(emd_data.embedding.getForFloatArray(emd_data.embedding_size.get()).length);
//            System.out.println(Arrays.toString(
//                    emd_data.embedding.getForFloatArray(emd_data.embedding_size.get()))
//            );

        }
    }

    @Test
    public void test_run2(){
        var input=new long[]{1,2,3,4};
        var attention_mask=new long[]{1,1,1,1};
        var token_type_ids=new long[]{0,0,0,0};

        try (Arena arena = Arena.ofShared()) {
            var model=new OnnxRtEmdModel()
                    .setEmdFramework(emdFramework);

            var run = new Runnable() {
                @Override
                public void run() {
                    var ctx=new OnnxRtEmdCtx(arena);
                    var emd_data=new OnnxRtEmdData(arena);
                            model.setDims(ctx,()-> List.of(
                                    new OnnxRtShape(arena).
                                            setName("input_ids").
                                            setData(input).
                                            setShape(new long[]{1,input.length}).
                                            input(),

                                    new OnnxRtShape(arena).
                                            setName("attention_mask").
                                            setData(attention_mask).
                                            setShape(new long[]{1,input.length}).
                                            input(),

                                    new OnnxRtShape(arena).
                                            setName("token_type_ids").
                                            setData(token_type_ids).
                                            setShape(new long[]{1,input.length}).
                                            input(),

                                    new OnnxRtShape(arena).
                                            setName("last_hidden_state").
                                            setData(new float[]{0}).
                                            setShape(new long[]{1,input.length,384}).
                                            output()
                            ));
                    model.forward(ctx,emd_data);
                    System.out.println("thread:"+Thread.currentThread().getName()+"start time: "+System.currentTimeMillis());
                    System.out.println("thread:"+Thread.currentThread().getName()+"result:"+emd_data.getEmbedding().length);
                    System.out.println("thread:"+Thread.currentThread().getName()+"end time: "+System.currentTimeMillis());
                }
            };
            var thread1=new Thread(run);
            var thread2=new Thread(run);
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
