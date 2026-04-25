package com.erling.core.load.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JdzFrameFFM {

    boolean useMappingConfig() default true;
    String mapping() default "";

    Class<?> nativeInterface() default void.class;

    String name() default "";
    String rootPath() default "";

}
