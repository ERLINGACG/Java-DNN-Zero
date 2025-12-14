package com.erling.jdz.cv.framework.yolo;

import com.erling.jdz.cv.frameworkinf.YoloFrameWorkInf;
import com.erling.jdz.load.DyLinkLibLoader;
import com.erling.jdz.load.ann.DyLinkLib;
import com.sun.jna.Pointer;

public class YoloFrameWork {
     @DyLinkLib(path = "clibconf/lib/GeneralDnnLib_Framework_Lib.dll")
     interface  IYoloFrameWork extends YoloFrameWorkInf{}

     private final IYoloFrameWork frameWork;

     Pointer yoloFrameworkPtr;
     public YoloFrameWork(){
        frameWork= DyLinkLibLoader.load(IYoloFrameWork.class);
        yoloFrameworkPtr=frameWork.CreateYoloFramework("clibconf/modelconf/yolo_config.json");
     }

     public void destroy(){
        frameWork.DestroyYoloFramework(yoloFrameworkPtr);
     }


     public void detect(byte[] byte_data, YoloOutput output){
        frameWork.YoloDetection(yoloFrameworkPtr, byte_data.length, byte_data, output);
        System.out.println(output.getJsonStr());
     }
}
