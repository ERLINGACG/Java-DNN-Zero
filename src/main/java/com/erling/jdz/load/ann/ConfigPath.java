package com.erling.jdz.load.ann;

import java.lang.annotation.Retention;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface ConfigPath {
    String value();
}
