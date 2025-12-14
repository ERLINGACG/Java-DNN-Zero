package com.erling.jdz.load.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DyLinkLib {
    String path() default "";

}
