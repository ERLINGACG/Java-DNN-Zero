package com.erling.ort.llm.model;

import com.erling.ort.llm.framework.ffm.OnnxRtLlmFramework;
import com.erling.ort.llm.model.rt.OnnxLLmModelOperationKt;
import com.erling.ort.llm.model.rt.RtCallBack;
import com.erling.ort.llm.struct.OnnxRtLlmCtx;
import com.erling.ort.llm.struct.OnnxRtLlmKv;
import com.erling.ort.llm.struct.OnnxRtLlmParam;
import com.erling.tokens.struct.TokenizerIDS;
import com.erling.tokens.struct.TokenizerLoader;
import com.erling.tokens.struct.TokenizerTokens;

import java.lang.foreign.Arena;
import java.util.function.Supplier;

public class OnnxLLmModel {



    private TokenizerLoader tokenizerLoader;
    private OnnxRtLlmFramework onnxRtLlmFramework;

    public OnnxLLmModel setTokenizerLoader(TokenizerLoader tokenizerLoader) {
        this.tokenizerLoader = tokenizerLoader;
        return this;
    }
    public OnnxLLmModel setOnnxRtLlmFramework(OnnxRtLlmFramework onnxRtLlmFramework) {
        this.onnxRtLlmFramework = onnxRtLlmFramework;
        return this;
    }

    public OnnxRtLlmFramework onnxRtLlmFramework() {
        return onnxRtLlmFramework;
    }

    public TokenizerLoader tokenizerLoader() {
        return tokenizerLoader;
    }

    public OnnxLLmModel setSample(OnnxRtLlmParam param, OnnxRtLlmCtx ctx){
        OnnxLLmModelOperationKt.setSample(
                this,param,ctx
        );
        return this;
    }

    public OnnxLLmModel prefillFor1DRoPE(Arena arena, OnnxRtLlmCtx ctx,OnnxRtLlmKv kv,long[] ids){
        OnnxLLmModelOperationKt.prefillFor1DRoPE(
                this,arena,ctx,kv,ids
        );
        return this;
    }

    public OnnxLLmModel decodeFor1DRoPE(OnnxRtLlmCtx ctx,OnnxRtLlmKv kv){
        OnnxLLmModelOperationKt.decodeFor1DRoPE(
                this,ctx,kv
        );
        return this;
    }

    public OnnxLLmModel resetCtx(OnnxRtLlmCtx ctx){
        OnnxLLmModelOperationKt.resetCtx(
                this,ctx
        );
        return this;
    }
    public OnnxLLmModel resetKv(OnnxRtLlmKv kv){
        OnnxLLmModelOperationKt.resetKv(
                this,kv
        );
        return this;
    }

    public OnnxLLmModel generate(String prompt, int eos,
                             Supplier<OnnxRtLlmParam>  paramSupplier,
                             Supplier<OnnxRtLlmCtx>    ctxSupplier,
                             Supplier<OnnxRtLlmKv>     kvSupplier,
                             RtCallBack                callBack
    ){
        try(Arena arena = Arena.ofShared()) {

            OnnxRtLlmParam param = paramSupplier.get();
            OnnxRtLlmCtx ctx = ctxSupplier.get();
            OnnxRtLlmKv kv = kvSupplier.get();
//            OnnxRtLlmKv kv = new OnnxRtLlmKv(arena);
            TokenizerIDS    tokenizerIDS = new TokenizerIDS(arena);
            TokenizerTokens tokenizerTokens = new TokenizerTokens(arena);
            setSample(param, ctx);
            int count = 0;
            prefillFor1DRoPE(arena, ctx, kv, tokenizerLoader().encodeForLong(tokenizerIDS, prompt));
            callBack.callback(tokenizerLoader.decode(tokenizerTokens,
                    Math.toIntExact(ctx.next_token_id.get())
                    ), count
            );
            while (true) {
                decodeFor1DRoPE(ctx, kv);
                if (!callBack.callback(tokenizerLoader.decode(tokenizerTokens,
                        Math.toIntExact(ctx.next_token_id.get())
                ), count)
                ) {
                    break;
                }
                if (ctx.next_token_id.get() == eos) {
                    break;
                }
                count++;
            }
//            resetKv(this,kv);
            tokenizerLoader().encodeIdsFree(tokenizerIDS);
            tokenizerLoader().decodeTokensFree(tokenizerTokens);
        }
        return this;
    }
}
