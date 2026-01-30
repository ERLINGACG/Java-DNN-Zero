package com.erling.jdz.cv.framework.face.feature;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

@Structure.FieldOrder({"feature_data","size"})
public class FeatureOutput extends Structure {
    public Pointer feature_data;
    public int size;

    public byte[] getDataAsBytes() {
        if (feature_data == null || size <= 0) {
            return new byte[0];
        }
        return feature_data.getByteArray(0, size);  // 从 data 指针偏移 0 处读取 size 个字节（unsigned char 对应 Java byte）
    }
}
