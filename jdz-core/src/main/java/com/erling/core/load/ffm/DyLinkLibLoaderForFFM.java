package com.erling.core.load.ffm;

import com.erling.core.load.ffm.api.loader.FFmLoaderKt;

public class DyLinkLibLoaderForFFM {

    public static void load(Object tagObject){
        synchronized (DyLinkLibLoaderForFFM.class) {   // 或者用 FFmLoaderKt.class
            FFmLoaderKt.load(tagObject);
        }
    }
}
