package com.erling.core.load.ffm.api.cpp.struct;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CArray {
    int len() default 0;
}
