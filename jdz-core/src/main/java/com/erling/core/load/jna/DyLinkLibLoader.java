package com.erling.core.load.jna;



public class DyLinkLibLoader {
    public static void Load(Object target){
        LoaderKt.loadLib(target);
    }
}
