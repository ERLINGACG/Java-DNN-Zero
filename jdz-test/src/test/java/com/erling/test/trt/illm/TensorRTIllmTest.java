package com.erling.test.trt.illm;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
import com.erling.core.load.ffm.api.cpp.hook.SetConfig;
import com.erling.trt.core.framework.ffm.cuda.TensorRTCuda;
import com.erling.trt.illm.framework.ffm.TensorRTIllmFramework;
import com.erling.trt.illm.rt.model.TensorRTBaseLlmModel;
import com.erling.trt.illm.struct.TensorRTIllmCtx;
import com.erling.trt.illm.struct.TensorRTIllmSampleParams;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;
import java.util.*;

public class TensorRTIllmTest {

    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GeneralDnnLib_Zero_trt_illm",
            useMappingConfig = false
    )
    @SetConfig(args = {"./libconfig/trt/trt_core_test.json"})
    public TensorRTIllmFramework tensorRTIllmFramework;

    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GeneralDnnLib_Zero_trt_core",
            useMappingConfig = false
    )
    public TensorRTCuda tensorRTCuda;

    public TensorRTIllmTest(){
        SetRunTimeEnv.SET.run();
        DyLinkLibLoaderForFFM.load(this);
    }

    @Test
    public void load(){


    }




    @Test
    public void test(){
        try(Arena arena = Arena.ofShared()){
            tensorRTIllmFramework.tensorRTIllmInf.GetTensorRTIllmEngineInfo(
                    tensorRTIllmFramework.framework
            );

        }
    }




    @Test
    public void test3(){
        int layer=28;
        var input_ids=new long[]{59248, 59250, 59252, 972, 5271, 417, 7133, 17898, 54869, 681, 59253, 972, 906, 15357, 5475, 50540, 10, 59254, 10};
        try(Arena arena = Arena.ofShared()){
            var ctx=new TensorRTIllmCtx(arena);
            var param=new TensorRTIllmSampleParams(arena);
            TensorRTBaseLlmModel model=new TensorRTBaseLlmModel().setTensorRTIllmFramework(tensorRTIllmFramework);
            model.registerInput("input_ids",    "INP").
                    registerInput("attention_mask", "MSK").
                    registerInput("position_ids", "POS").
                    registerInput(
                            "logits", "LOG"
                    ).
                    registerKvMapping(() -> {
                        Map<String, String> map=new HashMap<>();
                        for(int i=0;i<layer;i++){
                            map.put(STR."past_key_values.\{i}.key", STR."present.\{i}.key");
                            map.put(STR."past_key_values.\{i}.value", STR."present.\{i}.value");
                        }
                        return map;
                    }
            ).createLlmCtx(ctx).
                    setSampler(ctx,param).
                    autoInputIds(ctx,arena,input_ids).
                    autoPastKvCacheDefault(ctx,arena,new long[]{1,4,0,128}).
                    autoPresentKvCacheDefault(ctx).
                    autoLogits(ctx);

            List<Long> tokens=new ArrayList<>();
            List<Long> times=new ArrayList<>();
            var start_time = System.currentTimeMillis();
            model.prefillDefault(ctx);
            tokens.add(ctx.next_token.get());
//            for(int i=0;i<20;i++){
            var end_time = System.currentTimeMillis();
            times.add(end_time-start_time);

            while (ctx.next_token.get()!=59253){
                start_time = System.currentTimeMillis();
                model.decodeDefault(ctx);
                tokens.add(ctx.next_token.get());
                end_time = System.currentTimeMillis();
                times.add(end_time-start_time);
            }

            System.out.println("tokens = "+tokens);
            System.out.println("times = "+times);
        }
    }
}
