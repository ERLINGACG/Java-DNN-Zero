package com.erling.trt.rag.framework.ffm

import com.erling.trt.rag.struct.TrtEmbData
import java.lang.foreign.Arena
import java.lang.foreign.ValueLayout

fun forward(TrtEmbFrameFFM: TrtEmbFrameFFM, tokenizerID: List<Int>) : FloatArray {

//    Arena.ofConfined().use{arena ->
//
//        val inputSegment = arena.allocateArray(ValueLayout.JAVA_INT, *tokenizerID.toIntArray())
//        val embData = TrtEmbData(arena)
//        TrtEmbFrameFFM.frameInf.Forward(
//                TrtEmbFrameFFM.modelSegment(),
//                inputSegment,tokenizerID.size,
//                embData.getMemorySegment()
//        )
//
//        val embedding = embData.embedding.getForFloatArray(embData.embedding_size.get().toLong())
//        return embedding
//    }
    return FloatArray(20)
}
