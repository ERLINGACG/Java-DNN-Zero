package com.erling.test.llama;

import com.erling.llama.llm.function.ann.FuncParam;
import com.erling.llama.llm.function.ann.FuncTools;
import com.erling.llama.llm.function.kt.ScanFunctionKt;
import org.junit.jupiter.api.Test;

public class FunctionCallingTest {


    @Test
    public void testFunctionCalling() {
       var pari= ScanFunctionKt.scanFunctionGetInfo(()->{
            class TestClass {
                @FuncTools(description = "测试方法")
                public void test(
                        @FuncParam(description = "参数a")int a,
                        @FuncParam(description = "参数b")int b
                ) {}

            }
            return new TestClass();
        });

       System.out.println(pari.component1());
    }
}
