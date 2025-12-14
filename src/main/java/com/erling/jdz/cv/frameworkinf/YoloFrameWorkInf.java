package com.erling.jdz.cv.frameworkinf;

import com.erling.jdz.cv.framework.yolo.YoloOutput;
import com.sun.jna.Library;
import com.sun.jna.Pointer;
/**
 *     GDLZ_CORE_API yolo::YoloFramework* CreateYoloFramework(const char* conf_path);
 *     GDLZ_CORE_API void                 DestroyYoloFramework(const yolo::YoloFramework* framework);
 *     GDLZ_CORE_API void                 YoloDetection(yolo::YoloFramework* framework,
 *                                                      int size,unsigned char* byte,
 *                                                      data::YoloOutput& output);*/
public interface YoloFrameWorkInf extends Library {

    Pointer CreateYoloFramework(String configPath);
    void   DestroyYoloFramework(Pointer yoloFramework);

     void YoloDetection(Pointer yoloFramework, int size, byte[] byte_data, YoloOutput output);
}
