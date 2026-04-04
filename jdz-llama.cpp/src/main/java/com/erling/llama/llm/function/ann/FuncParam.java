package com.erling.llama.llm.function.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FuncParam {

   String description() default "";
}
