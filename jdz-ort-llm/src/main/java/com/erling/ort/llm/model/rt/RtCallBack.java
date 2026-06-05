package com.erling.ort.llm.model.rt;

public interface RtCallBack {
    boolean callback(String token_str,int token_count);


}
