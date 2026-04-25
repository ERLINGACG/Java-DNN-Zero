package com.erling.test.ffm;

import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
import com.erling.test.ffm.exm.StructChar;
import com.erling.test.ffm.exm.StructObj;
import com.erling.test.ffm.exm.StructObjV2;
import com.erling.test.ffm.exm.TestNaClass;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;

public class LoadForStructTest {

    @JdzFrameFFM(
            rootPath = "./libconfig/share",
            name = "GeneralDnnLib_Zero_plugin_Json_v2",
            useMappingConfig = false
    )
    TestNaClass testNaClass;

    public LoadForStructTest() {
        DyLinkLibLoaderForFFM.load(this);
    }


    @Test
    public void testLoadForStruct() {
        StructObj structObj = new StructObj(Arena.ofConfined());
        testNaClass.SetStruct(structObj.getMemorySegment());
        testNaClass.PrintCppTestStruct(structObj.getMemorySegment());
    }

    @Test
    public void testLoadForStructWithFields() {
        StructObj structObj = new StructObj(Arena.ofConfined());
//        structObj.setName("test");
//        structObj.setA(1);
//        structObj.setB(2);

        testNaClass.PrintCppTestStruct(structObj.getMemorySegment());
    }


    @Test
    public void testLoadForStructWithFieldsAndGetters() {
        StructObj structObj = new StructObj(Arena.ofConfined());

//        testNaClass.SetStruct(structObj.getMemorySegment());
//        System.out.println(structObj.getA());
//        System.out.println(structObj.getB());
//        System.out.println(structObj.getName());

    }

    @Test
    public void testStructChar() {
        StructChar structChar = new StructChar(Arena.ofConfined());
//        structChar.name.set("test");
        structChar.name.set("test".getBytes());
//        testNaClass.SetStruct2(structChar.getMemorySegment());
        testNaClass.PrintCppTestStruct2(structChar.getMemorySegment());
//        System.out.println(structChar.getName());
    }

    @Test
    public void testStructRegisterV2() {
        StructObjV2 structObjV2 = new StructObjV2(Arena.ofConfined());
        structObjV2.name.set("test111");
        structObjV2.a.set(1);
        structObjV2.b.set(2);
        testNaClass.PrintCppTestStruct(structObjV2.getMemorySegment());
        System.out.println(structObjV2.name.getForString());
    }

}
