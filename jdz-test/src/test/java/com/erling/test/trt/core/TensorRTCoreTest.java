package com.erling.test.trt.core;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
import com.erling.trt.core.framework.ffm.TensorRTCoreFramework;
import com.erling.trt.core.framework.ffm.cuda.TensorRTCuda;
import com.erling.trt.core.struct.cuda.TensorRTCudaAddress;
import com.erling.trt.core.struct.cuda.TensorRTCudaData;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;
import java.lang.foreign.ValueLayout;
import java.util.Arrays;

public class TensorRTCoreTest {


    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GeneralDnnLib_Zero_trt_core",
            useMappingConfig = false
    )
    public TensorRTCoreFramework tensorRTCoreFramework;

    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GeneralDnnLib_Zero_trt_core",
            useMappingConfig = false
    )
    public TensorRTCuda tensorRTCuda;

    public TensorRTCoreTest(){
        SetRunTimeEnv.SET.run();
        DyLinkLibLoaderForFFM.load(this);
    }

    @Test
    public void load(){}

    @Test
    public void test(){
        try(Arena arena = Arena.ofShared()){
//            tensorRTCoreFramework.tensorRTCoreInf.TestLoad(
//                    arena.allocateUtf8String("./libconfig/trt/trt_core_test.json")
//            );
        }

    }
    @Test
    public void test2(){
//        try(Arena arena = Arena.ofShared()){
//            tensorRTCoreFramework.tensorRTCoreInf.TestLoad2(
//                    arena.allocateUtf8String("./libconfig/trt/trt_core_test.json")
//            );
//        }

    }

    @Test
    public void test_int32(){
        int[] arr = new int[]{1,2,3,4,5};
        try(Arena arena = Arena.ofShared()){

            var address = new TensorRTCudaAddress(arena);
            var data =    new TensorRTCudaData(arena);

            var ret = tensorRTCuda.tensorRTCuInf.PostCudaINT32(
                    arena.allocateArray(ValueLayout.JAVA_INT,arr),
                    arr.length,
                    address.getMemorySegment()
            );
            ret = tensorRTCuda.tensorRTCuInf.GetCudaINT32(
                    data.getMemorySegment(),
                    address.getMemorySegment()
            );
            System.out.println(ret);
            System.out.println(Arrays.toString(data.data.getForIntArrayArray(data.len.get())));
        }

    }
    @Test
    public void test_int64(){
        long[] arr = new long[]{1,2,3,4,5};
        long[] dummy = new long[1];
        try (Arena arena = Arena.ofShared()) {
            var addr = new TensorRTCudaAddress(arena);
            var data = new TensorRTCudaData(arena);
            tensorRTCuda.tensorRTCuInf.PostCudaINT64(
                    arena.allocateArray(ValueLayout.JAVA_LONG, dummy),
                    1,
                    addr.getMemorySegment()
            );
        }
        try(Arena arena = Arena.ofShared()){

            var address = new TensorRTCudaAddress(arena);
            var data =    new TensorRTCudaData(arena);

            long start = System.currentTimeMillis();
            var ret = tensorRTCuda.tensorRTCuInf.PostCudaINT64(
                    arena.allocateArray(ValueLayout.JAVA_LONG,arr),
                    arr.length,
                    address.getMemorySegment()
            );
            System.out.println("post time"+(System.currentTimeMillis() - start));
            start = System.currentTimeMillis();
            ret = tensorRTCuda.tensorRTCuInf.GetCudaINT64(
                    data.getMemorySegment(),
                    address.getMemorySegment()
            );
            System.out.println("get time"+(System.currentTimeMillis() - start));


            System.out.println(ret);
            System.out.println(Arrays.toString(data.data.getForLongArray(data.len.get())));
        }

    }
}
