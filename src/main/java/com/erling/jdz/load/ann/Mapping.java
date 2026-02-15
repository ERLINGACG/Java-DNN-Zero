package com.erling.jdz.load.ann;

import com.sun.jna.Library;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {
    Class<? extends Library> value() ;
}
