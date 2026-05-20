package com.erling.ort.core.framework.ffm;

import java.lang.foreign.MemorySegment;

public interface OnnxCoreFrameWorkInf {
     MemorySegment CreateOnnxRtFramework(MemorySegment path);


     int CreateEngine(MemorySegment frameWork,MemorySegment engine);

     void Run(MemorySegment framework, MemorySegment engine);

     void GetEngineInfo(MemorySegment framework, MemorySegment engine);
}
