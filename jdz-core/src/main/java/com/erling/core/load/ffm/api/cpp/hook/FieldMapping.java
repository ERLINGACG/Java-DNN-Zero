package com.erling.core.load.ffm.api.cpp.hook;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldMapping {
    String name() default "";
    long offset() default 0;
}
