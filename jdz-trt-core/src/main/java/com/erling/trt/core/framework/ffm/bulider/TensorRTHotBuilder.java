package com.erling.trt.core.framework.ffm.bulider;

import com.erling.trt.core.framework.ffm.TensorRTCoreFramework;
import com.erling.trt.core.struct.rt.TensorRtBuilderEngineInfo;
import com.erling.trt.core.struct.rt.TensorRtBuilderShapeInfo;
import com.erling.trt.core.struct.rt.data.TensorRtExportEngine;

import java.util.function.Supplier;

public class TensorRTHotBuilder {

    public TensorRTCoreFramework framework;

    public TensorRTHotBuilder setFramework(TensorRTCoreFramework framework){
        this.framework = framework;
        return this;
    }

    public void checkFramework(){
        if(this.framework==null){
                throw new IllegalArgumentException("framework is null");
        }
    }

    public TensorRTHotBuilder initBuilderEngineInfo(TensorRtBuilderEngineInfo builderEngineInfo){
        checkFramework();
        int ret=this.framework.tensorRTCoreInf.InitBuilderEngineInfo(builderEngineInfo.getMemorySegment());
        if(ret!=0){
                throw new RuntimeException("InitBuilderEngineInfo failed, code:"+ret);
        }
        return this;
    }
    public TensorRTHotBuilder registerShapeInfo(TensorRtBuilderEngineInfo builderEngineInfo, Supplier<TensorRtBuilderShapeInfo> shapeInfoSupplier){
        checkFramework();
        int ret=this.framework.tensorRTCoreInf.RegisterShapeInfo(shapeInfoSupplier.get().getMemorySegment(), builderEngineInfo.getMemorySegment());
        if(ret!=0){
                throw new RuntimeException("RegisterShapeInfo failed, code:"+ret);
        }
        return this;
    }

    public TensorRTHotBuilder getShapeInfoDetail(TensorRtBuilderEngineInfo builderEngineInfo){
        checkFramework();
        int ret=this.framework.tensorRTCoreInf.GetShapeInfoDetail(builderEngineInfo.getMemorySegment());
        if(ret!=0){
                throw new RuntimeException("GetShapeInfoDetail failed, code:"+ret);
        }
        return this;
    }

    public TensorRTHotBuilder initExportEngine(TensorRtExportEngine exportEngine){
        checkFramework();
        int ret=this.framework.tensorRTCoreInf.InitBuilderEngine(exportEngine.getMemorySegment());
        if(ret!=0){
                throw new RuntimeException("InitBuilderEngine failed, code:"+ret);
        }
        return this;
    }

    public TensorRTHotBuilder buildEngine(TensorRtExportEngine exportEngine,String configPath,
                                          TensorRtBuilderEngineInfo builderEngineInfo){
        checkFramework();
        int ret=this.framework.tensorRTCoreInf.BuildEngine(
                exportEngine.getMemorySegment(),
                exportEngine.getArena().allocateUtf8String(configPath),
                builderEngineInfo.getMemorySegment()
        );
        if(ret!=0){
                throw new RuntimeException("BuildEngine failed, code:"+ret);
        }
        return this;
    }



}
