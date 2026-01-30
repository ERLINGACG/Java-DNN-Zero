package com.erling.jdz.cv.frameworkinf.face.utlis;

import com.sun.jna.Library;

public interface UtilsFrameWorkInf extends Library {

    void GetFeatureVec512(byte[] data,int size);
    double CompareVec512(byte[] d1,byte[] d2);
}
