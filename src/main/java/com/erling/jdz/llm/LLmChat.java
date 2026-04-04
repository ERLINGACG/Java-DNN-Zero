package com.erling.jdz.llm;

public interface LLmChat {

    String chat(String prompt, int maxTokens, ContextInit<?> context, RunTimeInit<?> runTimeInit);

    String charExample(String prompt, int maxTokens, ContextInit<?> context, RunTimeInit<?> runTimeInit, String gbnf);


}
