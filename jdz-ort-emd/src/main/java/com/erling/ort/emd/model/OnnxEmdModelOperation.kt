package com.erling.ort.emd.model

import com.erling.ort.core.struct.OnnxRtShape
import com.erling.ort.emd.struct.OnnxRtEmdCtx
import com.erling.ort.emd.struct.OnnxRtEmdData

fun setDims(model: OnnxRtEmdModel,shapeArray: List<OnnxRtShape>,ctx: OnnxRtEmdCtx): OnnxRtEmdModel{
    shapeArray.forEach {
        model.emdFramework()
            .emdInf.
            SetShape(it.memorySegment,ctx.memorySegment)
    }
    return model
}

fun forward(model: OnnxRtEmdModel,ctx: OnnxRtEmdCtx,data: OnnxRtEmdData): OnnxRtEmdModel{
    model.emdFramework()
        .emdInf.
        Forward(model.emdFramework().emdFramework(),
            ctx.memorySegment,
            data.memorySegment
        )
    return model
}
