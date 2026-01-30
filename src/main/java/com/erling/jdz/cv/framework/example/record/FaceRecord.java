package com.erling.jdz.cv.framework.example.record;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

@Structure.FieldOrder({"Face_Detector","Face_Feature_Extractor"})
public class FaceRecord extends Structure {

    public Pointer Face_Detector;
    public Pointer Face_Feature_Extractor;

}
