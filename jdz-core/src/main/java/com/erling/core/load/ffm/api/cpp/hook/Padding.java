package com.erling.core.load.ffm.api.cpp.hook;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Padding {
    int value() default 0;
}
