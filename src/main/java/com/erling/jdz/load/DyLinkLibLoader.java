package com.erling.jdz.load;

import com.erling.jdz.load.ann.DyLinkLib;
import com.sun.jna.Library;
import com.sun.jna.Native;

public class DyLinkLibLoader {

    @SuppressWarnings("unchecked")
    public static <T extends Library> T load(Class<? extends Library> libraryClass){
        DyLinkLib dyLinkLib=libraryClass.getAnnotation(DyLinkLib.class);

        if(dyLinkLib==null || dyLinkLib.path().isEmpty()){
            throw new IllegalArgumentException("DyLinkLib annotation not found");
        }
        try {
            return (T) Native.load(
                    (dyLinkLib.path()),
                    libraryClass
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to load library: " + dyLinkLib.path(), e);
        }
    }

}
