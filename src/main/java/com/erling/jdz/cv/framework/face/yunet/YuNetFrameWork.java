package com.erling.jdz.cv.framework.face.yunet;

import com.erling.jdz.base.FrameWorkBase;
import com.erling.jdz.cv.framework.face.arc.ArcFrameWork;
import com.erling.jdz.cv.framework.face.feature.FeatureOutput;
import com.erling.jdz.cv.frameworkinf.face.yunet.YuNetFrameWorkIf;
import com.erling.jdz.load.DyLinkLibLoader;
import com.erling.jdz.load.LoadMode;
import com.erling.jdz.load.ann.DyLinkLib;
import com.erling.jdz.load.ann.init;
import com.sun.jna.Pointer;

public class YuNetFrameWork implements  Cloneable  {




     private YuNetFrameWorkIf frameWork;

    public Pointer yunetFrameworkPtr() {
        return yunetFrameworkPtr;
    }

    Pointer yunetFrameworkPtr;

    private final String configPath;

    public YuNetFrameWork(String configPath){
        this.configPath=configPath;
    }

    @init
    public void init_model(){
        yunetFrameworkPtr=frameWork.CreateYuNetFramework(this.configPath);
    }


      public void destroy(){
        frameWork.DestroyYuNetFramework(yunetFrameworkPtr);
     }

     public void detection(int size , byte[] data,YuNetOutput output){
         frameWork.YuNetDetection(yunetFrameworkPtr, size, data,output);
      }

       public void featureVecArc(ArcFrameWork arcFrameWork, int size , byte[] data, YuNetOutput output0, FeatureOutput output1){
         frameWork.CV_YuNetDetection_FeatureVec_Arc(yunetFrameworkPtr,
                 arcFrameWork.arcFaceFrameworkPtr(), size, data,output0,output1);
      }

      @Override
      public YuNetFrameWork clone() throws CloneNotSupportedException {
        var copy=(YuNetFrameWork) super.clone();
        copy.init_model();
        return copy;
      }


}
