package com.erling.trt.core.framework.ffm.cuda;

import java.lang.foreign.MemorySegment;

public interface TensorRTCuInf {
    int PostCudaINT32(MemorySegment data,long size,MemorySegment address);

    int GetCudaINT32(MemorySegment data,MemorySegment address);

    int PostCudaINT64(MemorySegment data,long size,MemorySegment address);

    int PostCudaFLOAT32(MemorySegment data,long size,MemorySegment address);

    int SetAddressStream(MemorySegment dit,MemorySegment src);

    int GetCudaINT64(MemorySegment data,MemorySegment address);


}
