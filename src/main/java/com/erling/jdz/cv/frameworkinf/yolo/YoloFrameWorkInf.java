package com.erling.jdz.cv.frameworkinf.yolo;

import com.erling.jdz.cv.framework.yolo.YoloOutput;
import com.sun.jna.Library;
import com.sun.jna.Pointer;
/**
 * YOLO 框架核心 API 声明（C++ 层）：
 * <pre>
 * // 1. 创建 YOLO 框架实例
 * GDLZ_CORE_API yolo::YoloFramework* CreateYoloFramework(const char* conf_path);
 *
 * // 2. 销毁 YOLO 框架实例
 * GDLZ_CORE_API void DestroyYoloFramework(const yolo::YoloFramework* framework);
 *
 * // 3. 执行 YOLO 目标检测
 * GDLZ_CORE_API void YoloDetection(yolo::YoloFramework* framework,
 *                                  int size,
 *                                  unsigned char* byte,
 *                                  data::YoloOutput& output);
 * </pre>
 */
public interface YoloFrameWorkInf extends Library {

    Pointer CreateYoloFramework(String configPath);
    void   DestroyYoloFramework(Pointer yoloFramework);

    void YoloDetection(Pointer yoloFramework, int size, byte[] byte_data, YoloOutput output);
}
