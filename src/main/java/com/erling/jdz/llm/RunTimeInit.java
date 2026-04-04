package com.erling.jdz.llm;

@FunctionalInterface
public interface RunTimeInit<T> {

     T run();
}
