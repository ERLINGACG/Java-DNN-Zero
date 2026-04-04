package com.erling.test;

import com.erling.core.load.ann.DyLinkLibInf;
import com.sun.jna.Library;

public class CVTestExample {

    @DyLinkLibInf
    TestLib lib1;

    public void test1() {
        lib1.hello();
    }

}
