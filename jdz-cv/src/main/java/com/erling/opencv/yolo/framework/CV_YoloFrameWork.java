package com.erling.opencv.yolo.framework;

import com.erling.core.load.ann.DyLinkLibInf;
import com.erling.core.load.ann.InitType;
import com.erling.opencv.yolo.sturct.YoloOutput;
import com.sun.jna.Pointer;

public class CV_YoloFrameWork implements Cloneable{

    private final String configPath;
    @DyLinkLibInf
    private CV_YoloFrameWorkInf frameWork;

    Pointer yoloFrameworkPtr;

    public CV_YoloFrameWork(String configPath){
        this.configPath=configPath;
    }

    @InitType
    public void init_model(){
        this.yoloFrameworkPtr = frameWork.CreateYoloFramework(this.configPath);
    }

    public void destroy(){
        frameWork.DestroyYoloFramework(this.yoloFrameworkPtr);
    }

    public void detect(byte[] byte_data, YoloOutput output) {
        frameWork.YoloDetection(yoloFrameworkPtr, byte_data.length, byte_data, output);
    }

    public CV_YoloFrameWork clone() throws CloneNotSupportedException {
        CV_YoloFrameWork new_obj = (CV_YoloFrameWork) super.clone();
        new_obj.init_model();
        return new_obj;
    }

}
