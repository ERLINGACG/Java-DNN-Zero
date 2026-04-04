package com.erling.core.load.ann;


import com.sun.jna.Library;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JdzFramework {
    String rootPath() default "";

    String name() default "";
    Class<? extends Library> mapping() default Library.class;

    boolean isConfig() default false;

    String configPath() default "";


}
