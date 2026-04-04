package com.erling.opencv.yolo.sturct;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

@Structure.FieldOrder({"byte_data","json_str","json_size","size"})
public class YoloOutput extends Structure {
    public Pointer byte_data;
    public Pointer json_str;
    public int json_size;

    public int size;

    public byte[] getDataAsBytes() {
        if (byte_data == null || size <= 0) {
            return new byte[0];
        }
        return byte_data.getByteArray(0, size);  // 从 data 指针偏移 0 处读取 size 个字节（unsigned char 对应 Java byte）
    }

    public String getJsonStr() {
        if (json_str == null || json_size <= 0) {
            return "";
        }
        return json_str.getString(0, String.valueOf(json_size));  // 从 json_str 指针偏移 0 处读取 json_size 个字节（char 对应 Java char）
    }

}