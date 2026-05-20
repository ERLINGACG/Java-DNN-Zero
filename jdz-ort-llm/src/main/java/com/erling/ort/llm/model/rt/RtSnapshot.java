package com.erling.ort.llm.model.rt;

import com.erling.ort.llm.struct.OnnxRtLlmCtx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RtSnapshot {

    List<Long> historyInputIds=new ArrayList<>();

    public long[] nowInputIds;

    public long[] attentionMask;

    public long[] positionIds;

    public long[] build(long[] inputIds){
        historyInputIds.addAll(Arrays.stream(inputIds).boxed().toList());
        nowInputIds=inputIds;
        attentionMask=new long[inputIds.length];
        return nowInputIds;
    }
    public long[] buildMask(){
        Arrays.fill(attentionMask,1L);
        return attentionMask;
    }

    public long[] buildPositionIds(OnnxRtLlmCtx ctx){
        positionIds=new long[nowInputIds.length];
        if(ctx.token_pos.get()==0){
            for(int i=0;i<nowInputIds.length;++i){
                positionIds[i]=i;
            }
        }else{
            for(int i=0;i<nowInputIds.length;++i){
                positionIds[i]=i+ctx.token_pos.get();
            }
        }
        ctx.token_pos.set(ctx.token_pos.get()+nowInputIds.length);
        return positionIds;
    }

}
