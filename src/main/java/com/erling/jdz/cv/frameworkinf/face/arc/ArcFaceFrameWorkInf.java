package com.erling.jdz.cv.frameworkinf.face.arc;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

public interface ArcFaceFrameWorkInf extends Library {

    Pointer CreateArcFaceFramework(String conf_path);

    void DestroyArcFaceFramework(Pointer framework);
}
