package com.erling.jdz.cv.framework.face.arc;


import com.erling.jdz.cv.frameworkinf.face.arc.ArcFaceFrameWorkInf;
import com.erling.jdz.load.DyLinkLibLoader;
import com.erling.jdz.load.LoadMode;
import com.erling.jdz.load.ann.DyLinkLib;
import com.erling.jdz.load.ann.init;
import com.sun.jna.Pointer;

public class ArcFrameWork {

//    @DyLinkLib(path = "./clibconf/lib/GeneralDnnLib_Framework_Lib.dll")
//    interface IArcFaceFrameWorkInf extends ArcFaceFrameWorkInf {}

    ArcFaceFrameWorkInf arcFaceFrameWorkInf;

    public Pointer arcFaceFrameworkPtr() {
        return arcFaceFrameworkPtr;
    }

    Pointer arcFaceFrameworkPtr;

    private final String configPath;



    public ArcFrameWork(String configPath){
        this.configPath=configPath;
    }

    @init
    public void init_model(){
        arcFaceFrameworkPtr=arcFaceFrameWorkInf.CreateArcFaceFramework(this.configPath);
    }



    public void destroy(){
        arcFaceFrameWorkInf.DestroyArcFaceFramework(arcFaceFrameworkPtr);
    }
}
