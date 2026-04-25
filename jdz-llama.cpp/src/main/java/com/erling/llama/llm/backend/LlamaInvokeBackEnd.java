package com.erling.llama.llm.backend;

import java.util.function.Supplier;

public interface LlamaInvokeBackEnd<C,P> {
    void recording(
            String prompt,
            int eos,
            boolean clear,
            Supplier<C> ctx,
            Supplier<P> params,
            LlamaCallBack callBack
    );

    void init_model();
    void destroy_model();

    float[] embeddings(
            String prompt,boolean clear,
            Supplier<C> ctx,
            Supplier<P> params
    );
}
