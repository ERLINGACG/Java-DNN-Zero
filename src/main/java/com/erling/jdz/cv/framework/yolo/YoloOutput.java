package com.erling.jdz.cv.framework.yolo;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;


/**
 * JNA映射C++的YoloOutput结构体
 * <p>
 * C++侧原始定义：
 * <pre>
 * struct YoloOutput {
 *
 *     std::unique_ptr<unsigned char[]> byte_data{};// 编码后的图片字节数据（unsigned char[]）
 *     std::unique_ptr<char[]> json_str{}; // 检测结果JSON字符串（char[]，UTF-8编码）
 *
 *     int json_size{};// JSON字符串有效长度（不含末尾\0）
 *     int size{};// 图片字节数据长度
 *
 *     // 填充数据的核心方法
 *     auto& SetData(const cv::Mat& mat, const std::string& json_str_) {
 *         const std::vector params_ {cv::IMWRITE_WEBP_QUALITY, 95};
 *         std::vector<unsigned char> encoded;
 *         cv::imencode(".jpeg", mat, encoded, params_);
 *         size = static_cast<int>(encoded.size());
 *         byte_data = std::make_unique<unsigned char[]>(encoded.size());
 *         memcpy(byte_data.get(), encoded.data(), encoded.size());
 *
 *         auto buffer = std::make_unique<char[]>(json_str_.size() + 1);
 *         std::ranges::copy(json_str_, buffer.get());
 *         buffer[json_str_.size()] = '\0'; // 手动添加null终止符
 *         json_str = std::move(buffer);
 *         json_size = static_cast<int>(json_str_.size());
 *         return *this;
 *     }
 * };
 * </pre>
 *
 * @author 巴别塔の饿灵（ERLING）
 * &#064;date  2025/12/14
 */
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
