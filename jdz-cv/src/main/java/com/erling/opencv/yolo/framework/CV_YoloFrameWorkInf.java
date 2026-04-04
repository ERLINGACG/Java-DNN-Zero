package com.erling.opencv.yolo.framework;

import com.erling.opencv.yolo.sturct.YoloOutput;
import com.sun.jna.Library;
import com.sun.jna.Pointer;

public interface CV_YoloFrameWorkInf extends Library {
    Pointer CreateYoloFramework(String configPath);
    void   DestroyYoloFramework(Pointer yoloFramework);
    void YoloDetection(Pointer yoloFramework, int size, byte[] byte_data, YoloOutput output);
}
