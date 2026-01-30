package com.erling.jdz.cv.framework.face.yunet;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

//struct YunetOutPut
//    {
//std::unique_ptr<unsigned char[]> byte_data{};
//int size{};
//
//auto& SetData(const cv::Mat& mat)
//{
//            const std::vector params_ {cv::IMWRITE_WEBP_QUALITY, 95};
//    std::vector<unsigned char> encoded;
//    cv::imencode(".jpeg", mat, encoded, params_);
//    size =  static_cast<int>(encoded.size());
//    byte_data = std::make_unique<unsigned char[]>(encoded.size());
//    memcpy(byte_data.get(), encoded.data(), encoded.size());
//
//
//    // auto buffer = std::make_unique<char[]>(json_str_.size() + 1);
//    // std::ranges::copy(json_str_, buffer.get());
//    // buffer[json_str_.size()] = '\0'; // 手动添加 null 终止符
//    // json_str=std::move(buffer);
//    // json_size = static_cast<int>(json_str_.size());
//    return *this;
//}
//    };
@Structure.FieldOrder({"byte_data","size"})
public class YuNetOutput extends Structure {
    public Pointer byte_data;
    public int size;

    public byte[] getDataAsBytes() {
        if (byte_data == null || size <= 0) {
            return new byte[0];
        }
        return byte_data.getByteArray(0, size);  // 从 data 指针偏移 0 处读取 size 个字节（unsigned char 对应 Java byte）
    }
}
