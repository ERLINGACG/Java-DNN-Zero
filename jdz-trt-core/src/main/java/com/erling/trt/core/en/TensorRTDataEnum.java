package com.erling.trt.core.en;

import java.util.Map;

public enum TensorRTDataEnum {
    FLOAT32(0),
    FLOAT16(1),
    INT8(2),
    INT32(3),
    BOOL(4),
    UINT8(5),
    FP8(6),
    BF16(7),
    INT64(8),
    INT4(9),
    FP4(10),
    E8M0(11),;

    public final int value;

    public static final Map<TensorRTDataEnum, Integer> typeSizeMap = Map.ofEntries(
            Map.entry(FLOAT32, 4),
            Map.entry(FLOAT16, 2),
            Map.entry(INT8, 1),
            Map.entry(INT32, 4),
            Map.entry(BOOL, 1),
            Map.entry(UINT8, 1),
            Map.entry(FP8, 1),
            Map.entry(BF16, 2),
            Map.entry(INT64, 8),
            Map.entry(INT4, 1),   // 实际占4位，按1字节对齐
            Map.entry(FP4, 1),    // 实际占4位，按1字节对齐
            Map.entry(E8M0, 1)    // 8位指数无尾数，占1字节
    );
    TensorRTDataEnum(int value) {
        this.value = value;
    }

    public Integer fromTypeSize(TensorRTDataEnum dataType) {
        return typeSizeMap.get(dataType);
    }
}
