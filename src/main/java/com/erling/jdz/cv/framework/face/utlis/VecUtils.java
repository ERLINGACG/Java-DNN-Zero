package com.erling.jdz.cv.framework.face.utlis;

import com.erling.jdz.cv.frameworkinf.face.utlis.UtilsFrameWorkInf;

public class VecUtils {



    UtilsFrameWorkInf vecUtilsInf;

    public VecUtils(){

    }

    public void GetVec512(byte[] data){
        vecUtilsInf.GetFeatureVec512(data,data.length);
    }
    public void CompareVec512(byte[] d1 ,byte[] d2){
        System.out.println(vecUtilsInf.CompareVec512(d1,d2));
    }
}
