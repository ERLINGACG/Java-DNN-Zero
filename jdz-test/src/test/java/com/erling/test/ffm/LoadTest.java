package com.erling.test.ffm;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;

import com.erling.test.ffm.exm.CppLib;
import com.erling.test.ffm.exm.TestNaClass;
import com.erling.test.ffm.exm.TestStruct;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static com.erling.core.load.ffm.api.loader.FFmLoaderKt.createNativeProxyForJava;

public class LoadTest {

    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GeneralDnnLib_Zero_plugin_Json_v2",
            useMappingConfig = false
    )
    TestNaClass testNaClass;

    public LoadTest(){
        DyLinkLibLoaderForFFM.load(this);
    }



    @Test
    public void Load() throws Throwable {
//        int iterations = 1000;
//        long start = System.nanoTime();
//        for (int i = 0; i < iterations; i++) {
            testNaClass.CreateCppToJava();
            testNaClass.PrintCppToJava();
            testNaClass.PrintCppStr("你好，世界！");
            try (Arena arena = Arena.ofConfined()) {
                testNaClass.PrintCppTestStruct(
                        new TestStruct().createStruct(arena, "example", 10, 20));
            }
            testNaClass.DestroyCppToJava();
//
    }

    @Test
    public void testLoadHost() throws Throwable {
        // 1. 预热 JVM（让 JIT 优化代码）
        for (int i = 0; i < 10000; i++) {
            MemorySegment obj = (MemorySegment) testNaClass.getFunction("CreateCppToJava").invokeExact();
            testNaClass.getFunction("PrintCppToJava").invokeExact(obj);
            testNaClass.getFunction("DestroyCppToJava").invokeExact(obj);
        }

        // 2. 正式测试直接调用（多次取平均）
        int iterations = 1000;
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            MemorySegment obj = (MemorySegment) testNaClass.getFunction("CreateCppToJava").invokeExact();
            testNaClass.getFunction("PrintCppToJava").invokeExact(obj);
            testNaClass.getFunction("DestroyCppToJava").invokeExact(obj);
        }
        long directTime = System.nanoTime() - start;
        System.out.printf("Direct call avg: %.2f ns\n", directTime / (double) iterations);

        // 3. 预热代理
        CppLib cppLib = (CppLib) createNativeProxyForJava(CppLib.class, testNaClass.getFunctionMap());
        for (int i = 0; i < 10000; i++) {
            MemorySegment obj = cppLib.CreateCppToJava();
            cppLib.PrintCppToJava(obj);
            cppLib.DestroyCppToJava(obj);
        }

        // 4. 正式测试代理调用
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            MemorySegment obj = cppLib.CreateCppToJava();
            cppLib.PrintCppToJava(obj);
            cppLib.DestroyCppToJava(obj);
        }
        long proxyTime = System.nanoTime() - start;
        System.out.printf("Proxy call avg: %.2f ns\n", proxyTime / (double) iterations);
    }


}
