package com.erling.llama.llm.backend;

public interface LlamaCallBack {
    Boolean invoke(String token, int token_count,int tokenizer_len);
}
