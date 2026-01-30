package com.erling.jdz.uitls.run;

@FunctionalInterface
public interface TaskRun {
    void run(Object... obj) throws IllegalAccessException;

}
