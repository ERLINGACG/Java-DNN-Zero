package com.erling.jdz.llm;

@FunctionalInterface
public interface  ContextInit<T> {
    T run();
}
