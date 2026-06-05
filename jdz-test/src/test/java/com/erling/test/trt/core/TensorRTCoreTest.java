package com.erling.test.trt.core;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
import com.erling.trt.core.framework.ffm.TensorRTCoreFramework;
import org.junit.jupiter.api.Test;

public class TensorRTCoreTest {


    @JdzFrameFFM(

    )
    public TensorRTCoreFramework tensorRTCoreFramework;

    public TensorRTCoreTest(){
        SetRunTimeEnv.SET.run();
        DyLinkLibLoaderForFFM.load(this);
    }

    @Test
    public void test(){

    }
}
