package com.erling.trt.rag.model.data;

import com.erling.trt.rag.struct.TrtEmbCtx;

public record InputDims(TrtEmbCtx ctx, String dims_name, int[] dims) {
}
