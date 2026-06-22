package com.erling.test.trt.core;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
import com.erling.trt.core.framework.ffm.TensorRTCoreFramework;
import com.erling.trt.core.framework.ffm.bulider.TensorRTHotBuilder;
import com.erling.trt.core.struct.rt.TensorRtBuilderEngineInfo;
import com.erling.trt.core.struct.rt.TensorRtBuilderShapeInfo;
import com.erling.trt.core.struct.rt.data.TensorRtExportEngine;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;

public class TensorRtHotBuilderTest {


    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GeneralDnnLib_Zero_trt_core",
            useMappingConfig = false
    )
    public TensorRTCoreFramework tensorRTCoreFramework;



    public TensorRtHotBuilderTest(){
        SetRunTimeEnv.SET.run();
        DyLinkLibLoaderForFFM.load(this);
    }

    @Test
    public void hostBuilder(){
        try(Arena arena = Arena.ofShared()) {
            TensorRtBuilderEngineInfo builderEngineInfo = new TensorRtBuilderEngineInfo(arena);
            TensorRtExportEngine exportEngine = new TensorRtExportEngine(arena);

            TensorRTHotBuilder tensorRTHotBuilder = new TensorRTHotBuilder();
            tensorRTHotBuilder.
                    setFramework(tensorRTCoreFramework).
                    initBuilderEngineInfo(builderEngineInfo).
                    registerShapeInfo(builderEngineInfo, () ->
                            new TensorRtBuilderShapeInfo(arena).
                                    setName("input_ids").
                                    setK_min_dims(2L).
                                    setK_min_shape(new long[]{1L, 1L}).
                                    setK_opt_dims(2L).
                                    setK_opt_shape(new long[]{1L, 1024L}).
                                    setK_max_dims(2L).
                                    setK_max_shape(new long[]{1L, 4096L})
                    ).
                    registerShapeInfo(builderEngineInfo, () ->
                            new TensorRtBuilderShapeInfo(arena).
                                    setName("attention_mask").
                                    setK_min_dims(2L).
                                    setK_min_shape(new long[]{1L, 1L}).

                                    setK_opt_dims(2L).
                                    setK_opt_shape(new long[]{1L, 1024L}).
                                    setK_max_dims(2L).
                                    setK_max_shape(new long[]{1L, 4096L})
                    ).
                    registerShapeInfo(builderEngineInfo, () ->
                            new TensorRtBuilderShapeInfo(arena).
                                    setName("position_ids").
                                    setK_min_dims(2L).
                                    setK_min_shape(new long[]{1L, 1L}).
                                    setK_opt_dims(2L).
                                    setK_opt_shape(new long[]{1L, 1024L}).
                                    setK_max_dims(2L).
                                    setK_max_shape(new long[]{1L, 4096L})
                    );
                    for(int i=0;i<28;i++){
                        int finalI = i;
                        tensorRTHotBuilder.registerShapeInfo(
                                    builderEngineInfo,
                                    () ->
                                            new TensorRtBuilderShapeInfo(arena).
                                                    setName(STR."past_key_values.\{finalI}.key").
                                                    setK_min_dims(4L).
                                                    setK_min_shape(new long[]{1,4,0,128}).
                                                    setK_opt_dims(4L).
                                                    setK_opt_shape(new long[]{1,4,0,128}).
                                                    setK_max_dims(4L).
                                                    setK_max_shape(new long[]{1,4,4096,128})
                          ).
                                registerShapeInfo(
                                    builderEngineInfo,
                                    () ->
                                            new TensorRtBuilderShapeInfo(arena).
                                                    setName(STR."past_key_values.\{finalI}.value").
                                                    setK_min_dims(4L).
                                                    setK_min_shape(new long[]{1,4,0,128}).
                                                    setK_opt_dims(4L).
                                                    setK_opt_shape(new long[]{1,4,0,128}).
                                                    setK_max_dims(4L).
                                                    setK_max_shape(new long[]{1,4,4096,128})
                          );
                    }
                    tensorRTHotBuilder.getShapeInfoDetail(builderEngineInfo).
                    initExportEngine(exportEngine).
                    buildEngine(
                            exportEngine, "./libconfig/trt/trt_core_test.json",
                            builderEngineInfo
                    );
//            System.out.println(builderEngineInfo);
        }


    }
}
