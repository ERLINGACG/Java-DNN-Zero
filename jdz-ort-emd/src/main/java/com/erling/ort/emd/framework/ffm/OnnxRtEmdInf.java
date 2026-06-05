package com.erling.ort.emd.framework.ffm;

import java.lang.foreign.MemorySegment;

public interface OnnxRtEmdInf {
    MemorySegment CreateEmdFramework(MemorySegment config_path);

    void DestroyEmdFramework(MemorySegment emd_framework);

    void GetEmdFrameworkEngineInfo(MemorySegment emd_framework);

    int SetShape(MemorySegment shape, MemorySegment ctx);

    int Forward(MemorySegment emd_framework, MemorySegment ctx, MemorySegment emd_data);
}
