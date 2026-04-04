package com.erling.jdz.cv.framework.yolo;

import com.erling.jdz.cv.frameworkinf.yolo.YoloFrameWorkInf;
import com.erling.jdz.load.ann.init;
import com.sun.jna.Pointer;

public class YoloFrameWork  implements Cloneable {

     private YoloFrameWorkInf frameWork;

     Pointer yoloFrameworkPtr;

     private final String configPath;

     public YoloFrameWork(String configPath){
          this.configPath=configPath;
     }

     @init
     public void init_model(){
        this.yoloFrameworkPtr = frameWork.CreateYoloFramework(this.configPath);
     }

     public void destroy(){
        frameWork.DestroyYoloFramework(yoloFrameworkPtr);
     }


    public void detect(byte[] byte_data, YoloOutput output) {
        frameWork.YoloDetection(yoloFrameworkPtr, byte_data.length, byte_data, output);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        YoloFrameWork new_obj = (YoloFrameWork) super.clone();
        new_obj.init_model();
        return new_obj;
    }
}
