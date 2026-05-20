package com.erling.ort.llm.model;

import com.erling.ort.core.shape.OnnxMirrorShape;
import com.erling.ort.core.struct.OnnxRtInput;
import com.erling.ort.core.struct.OnnxRtShape;
import com.erling.ort.core.type.OnnxDataType;
import com.erling.ort.llm.framework.ffm.OnnxRtLlmFramework;
import com.erling.ort.llm.exce.OrtLLmFrameWorkIsNull;
import com.erling.ort.llm.model.rt.RtCallBack;
import com.erling.ort.llm.model.shape.BuildShapeKt;
import com.erling.ort.llm.struct.OnnxRtLlmCtx;
import com.erling.ort.llm.struct.OnnxRtLlmKv;
import com.erling.ort.llm.struct.OnnxRtLlmParam;
import com.erling.tokens.framework.TokensFramework;
import com.erling.tokens.struct.TokenizerIDS;
import com.erling.tokens.struct.TokenizerLoader;
import com.erling.tokens.struct.TokenizerTokens;

import java.lang.foreign.Arena;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class OnnxLLmModel {

    public OnnxLLmModel(){}

    private final Arena arena=Arena.global();



    private TokenizerLoader tokenizerLoader;

    private  TokensFramework tokenizerFramework;
    private OnnxRtLlmFramework onnxRtLlmFramework;

//    public List<OnnxMirrorShape<?>> inputShapes() {
//        return inputShapes;
//    }

//    private List<OnnxMirrorShape<?>> inputShapes=new ArrayList<>();


    public TokenizerLoader tokenizerLoader() {
        return tokenizerLoader;
    }


    public OnnxRtLlmFramework onnxRtFramework() {
        if (onnxRtLlmFramework==null){
            throw new OrtLLmFrameWorkIsNull("onnxRtLlmFramework is null");
        }
        return onnxRtLlmFramework;
    }

    public OnnxLLmModel setOnnxRtLlmFramework(OnnxRtLlmFramework onnxRtLlmFramework){
        this.onnxRtLlmFramework=onnxRtLlmFramework;
        if (this.onnxRtLlmFramework==null){
            throw new OrtLLmFrameWorkIsNull("onnxRtLlmFramework is null");
        }
        return this;
    }


    public OnnxLLmModel setTokenizer(TokensFramework tokenizerFramework){
        this.tokenizerLoader=new TokenizerLoader(arena);
        this.tokenizerFramework=tokenizerFramework;
        tokenizerFramework.loadTokenizer(this.tokenizerLoader.getMemorySegment());
        return this;
    }

    private long[] buildInputIds(String prompt){
         try(Arena arena=Arena.ofConfined()){

             var ids = new TokenizerIDS(arena);
             tokenizerFramework.encode(
                     this.tokenizerLoader.getMemorySegment(),
                     prompt,ids.getMemorySegment());

             return Arrays.stream(
                     ids.ids.getForIntArrayArray(ids.len.get())
             ).mapToLong(Long::valueOf).toArray();
         }
    }

    private String buildOutput(long token_id, Arena arena){
        var token = new TokenizerTokens(arena);
        tokenizerFramework.decodeToken(
                this.tokenizerLoader.getMemorySegment(),
                (int) token_id,
                token.getMemorySegment()
        );
        return token.tokens.getForString();
    }

    public OnnxLLmModel buildInputShape(
            String prompt,
            OnnxRtLlmCtx ctx,
            List<OnnxMirrorShape<?>> inputShapes

    ){

            var input  =ctx.addMemoryTokens(buildInputIds(prompt)).getMemoryTokens();
            var mask   =ctx.generateAttentionMask();
            var pos    =ctx.generatePositionIds();

            System.out.println(Arrays.toString(input));
            System.out.println(Arrays.toString(mask));
            System.out.println(Arrays.toString(pos));


            inputShapes.add(
                    new OnnxMirrorShape<>(
                            new long[]{ctx.batch.get(), input.length},
                            "AUTO",
                            OnnxDataType.INT64,
                            input
                    )
            );
            inputShapes.add(
                    new OnnxMirrorShape<>(
                            new long[]{ctx.batch.get(), mask.length},
                            "AUTO",
                            OnnxDataType.INT64,
                            mask
                    )
            );
            inputShapes.add(
                    new OnnxMirrorShape<>(
                            new long[]{ctx.batch.get(), pos.length},
                            "AUTO",
                            OnnxDataType.INT64,
                            pos
                    )
            );
        return this;
    }

    public OnnxLLmModel buildKvShape(
            Supplier<List<OnnxMirrorShape<?>>> shapeSupplier,
            List<OnnxMirrorShape<?>> inputShapes){
        var shapes=shapeSupplier.get();
        inputShapes.addAll(shapes);
        return this;
    }

    public OnnxLLmModel setSampler(Supplier<OnnxRtLlmCtx> ctxSupplier, Supplier<OnnxRtLlmParam> paramSupplier){
        var param=paramSupplier.get();
        var ctx=ctxSupplier.get();
        this.onnxRtLlmFramework.onnxRtLlmInf.InitSampler(
                param.getMemorySegment(),
                ctx.getMemorySegment()
        );
        return this;
    }

    public OnnxLLmModel buildAllInputShape(
            OnnxRtInput input,
            List<OnnxMirrorShape<?>> inputShapes,
            Arena arena
    ){
        BuildShapeKt.buildShape(this,arena,input,inputShapes);
        return this;
    }

    public OnnxLLmModel prefill(
            OnnxRtLlmCtx ctx, OnnxRtLlmKv kv, OnnxRtInput input

    ){


        this.onnxRtLlmFramework.onnxRtLlmInf.Prefill(
                this.onnxRtLlmFramework.framework(),
                ctx.getMemorySegment(),
                kv.getMemorySegment(),
                input.getMemorySegment()

        );
        return this;
    }

    public OnnxLLmModel decode(
            OnnxRtLlmCtx ctx, OnnxRtLlmKv kv, OnnxRtInput input,Arena arena
    ){


        var next_token_id=new long[]{ctx.next_token_id.get()};
        var next_token_attention=new long[]{1};
        var next_token_position=new long[]{ctx.token_pos.get()};

        this.onnxRtLlmFramework.onnxRtLlmInf.SetInput(
                this.onnxRtLlmFramework.framework(),
                new OnnxRtShape(arena).shape(new long[]{ctx.batch.get(),1})
                        .type(OnnxDataType.INT64.i()).data(next_token_id)
                        .getMemorySegment(),
                input.getMemorySegment()
        );
        this.onnxRtLlmFramework.onnxRtLlmInf.SetInput(
                this.onnxRtLlmFramework.framework(),
                new OnnxRtShape(arena).shape(new long[]{ctx.batch.get(),1})
                        .type(OnnxDataType.INT64.i()).data(next_token_attention)
                        .getMemorySegment(),
                input.getMemorySegment()
        );

        this.onnxRtLlmFramework.onnxRtLlmInf.SetInput(
                this.onnxRtLlmFramework.framework(),
                new OnnxRtShape(arena).shape(new long[]{ctx.batch.get(),1})
                        .type(OnnxDataType.INT64.i()).data(next_token_position)
                        .getMemorySegment(),
                input.getMemorySegment()
        );

        this.onnxRtLlmFramework.onnxRtLlmInf.Decode(
                this.onnxRtLlmFramework.framework(),
                ctx.getMemorySegment(),
                kv.getMemorySegment(),
                input.getMemorySegment()
        );
        return this;
    }

    public OnnxLLmModel chat(Supplier<OnnxRtLlmCtx> ctxSupplier, Supplier<OnnxRtLlmKv> kvSupplier,
                             Supplier<OnnxRtInput> inputSupplier,
                             Arena arena,
                             long eos,
                             RtCallBack callBack
                             ){
        var ctx=ctxSupplier.get();
        var input=inputSupplier.get();
        var kv=kvSupplier.get();

        this.prefill(ctx,kv,input);
        var output=this.buildOutput(ctx.next_token_id.get(), arena);
        int count=1;
        callBack.callback(output,1);
        while (ctx.next_token_id.get()!=eos){
             this.decode(ctx,kv,input,arena);
             output=this.buildOutput(ctx.next_token_id.get(), arena);
             if(!callBack.callback(output,count++)){
                 break;
             }
        }
        return this;
    }




}
