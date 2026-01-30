package com.erling.jdz.cv.framework.example.record;

import com.erling.jdz.cv.framework.face.arc.ArcFrameWork;
import com.erling.jdz.cv.framework.face.feature.FeatureOutput;
import com.erling.jdz.cv.framework.face.yunet.YuNetFrameWork;
import com.erling.jdz.cv.framework.face.yunet.YuNetOutput;
import com.erling.jdz.cv.framework.yolo.YoloFrameWork;
import com.erling.jdz.load.DyLinkLibLoader;
import com.erling.jdz.load.ann.DyLinkLib;
import com.sun.jna.Pointer;

public class ExampleCall {

    public void DeleteFaceRecord(FaceRecord record) {
        exampleINF.DeleteFaceRecord(record);
    }

    @DyLinkLib(path = "./clibconf/lib/GeneralDnnLib_Framework_Lib.dll")
    interface IExampleINF extends ExampleINF {}

    IExampleINF exampleINF;

    public ExampleCall() {
        exampleINF = DyLinkLibLoader.load(IExampleINF.class);
    }

    public void test(
            FaceRecord faceRecord,
//            Pointer faceDetectorPtr,
//            Pointer faceFeatureExtractorPtr,
            int size,
            byte[] data,
            YuNetOutput Output1,
            FeatureOutput Output2
    ){
//        exampleINF.AssemblyFaceRecord(faceRecord, faceDetectorPtr, faceFeatureExtractorPtr);
        exampleINF.AssemblyFaceRecord_1(faceRecord,
                "clibconf/modelconf/yunet_config.json",
                "clibconf/modelconf/arcface_config.json");
        exampleINF.ExampleFaceRecordDo(faceRecord, size, data, Output1, Output2);
    }
    public void test_2(
            FaceRecord faceRecord,
            Pointer faceDetectorPtr,
            Pointer faceFeatureExtractorPtr,
            int size,
            byte[] data,
            YuNetOutput Output1,
            FeatureOutput Output2
    ){
        exampleINF.AssemblyFaceRecord(
                faceRecord, faceDetectorPtr, faceFeatureExtractorPtr
        );
        exampleINF.ExampleFaceRecordDo(faceRecord, size, data, Output1, Output2);
    }

    public void test_3(
            FaceRecord faceRecord,
            YuNetFrameWork frameWork1,
            ArcFrameWork   frameWork2,
            int size,
            byte[] data,
            YuNetOutput Output1,
            FeatureOutput Output2
    ){
        exampleINF.AssemblyFaceRecord(
                faceRecord, frameWork1.yunetFrameworkPtr(), frameWork2.arcFaceFrameworkPtr()
        );
        frameWork1 = null;
        frameWork2 = null;
        exampleINF.ExampleFaceRecordDo(faceRecord, size, data, Output1, Output2);
    }
}
