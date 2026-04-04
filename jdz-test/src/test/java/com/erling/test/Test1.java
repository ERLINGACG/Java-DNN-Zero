package com.erling.test;

import com.erling.core.load.ann.JdzFramework;
import com.erling.core.load.jna.DyLinkLibLoader;
import org.junit.jupiter.api.Test;

public class Test1 {
    @JdzFramework(
            rootPath = "E:/ZeroPlan/Java/JDnn-Zero/JDnnZero/clibconf/lib/",
            name = "D1",
            mapping = TestLib.class
    )
    CVTestExample cvTestExample;

    @Test
    public void test1(){
        DyLinkLibLoader.Load(this);
        cvTestExample.test1();
    }
}
