package com.erling.jdz.cv.framework.example.record;

import com.erling.jdz.cv.framework.face.feature.FeatureOutput;
import com.erling.jdz.cv.framework.face.yunet.YuNetOutput;
import com.sun.jna.Library;
import com.sun.jna.Pointer;

public interface ExampleINF extends Library {

    void AssemblyFaceRecord(FaceRecord faceRecord, Pointer faceDetector, Pointer faceFeatureExtractor);
    void AssemblyFaceRecord_1(FaceRecord faceRecord, String configPath1, String configPath2);
    void  ExampleFaceRecordDo(FaceRecord faceRecord, int size, byte[] data, YuNetOutput Output1, FeatureOutput Output2);
    void DeleteFaceRecord(FaceRecord faceRecord);
}
