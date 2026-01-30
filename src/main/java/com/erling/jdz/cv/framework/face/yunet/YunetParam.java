package com.erling.jdz.cv.framework.face.yunet;

import com.erling.jdz.cv.framework.face.param.FaceParam;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.List;


@Structure.FieldOrder({"input", "blob", "faces", "rects"})
public class YunetParam extends Structure {

    // ✅ 按注解顺序，平铺声明全部4个指针字段，一一对应C++的4个智能指针
    public Pointer input;  // 对应 BaseParam 的 std::unique_ptr<cv::Mat> input;
    public Pointer blob;   // 对应 BaseParam 的 std::unique_ptr<cv::Mat> blob;
    public Pointer faces;  // 对应 FaceParam 的 std::unique_ptr<cv::Mat> faces;
    public Pointer rects;  // 对应 FaceParam 的 std::unique_ptr<std::vector<cv::Rect>> rects;

    // ✅ 必须保留的无参构造方法，JNA反射实例化用，加不加super()都可以
    public YunetParam() {
        super(Structure.ALIGN_NONE); // 重中之重！必加，解决C++虚析构的虚表指针内存对齐问题
    }

    // ✅ 无需重写 getFieldOrder() 方法！注解完全替代，一行都不用写
    // 彻底抛弃所有getFieldOrder相关的坑！！！
}
