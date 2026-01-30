package cv.framework;

import com.erling.jdz.cv.framework.face.arc.ArcFrameWork;
import com.erling.jdz.cv.framework.face.feature.FeatureOutput;
import com.erling.jdz.cv.framework.face.utlis.VecUtils;
import com.erling.jdz.cv.framework.face.yunet.YuNetFrameWork;
import com.erling.jdz.cv.framework.face.yunet.YuNetOutput;
import com.erling.jdz.cv.framework.yolo.YoloFrameWork;
import com.erling.jdz.cv.framework.yolo.YoloOutput;
import com.erling.jdz.cv.frameworkinf.face.arc.ArcFaceFrameWorkInf;
import com.erling.jdz.cv.frameworkinf.face.utlis.UtilsFrameWorkInf;
import com.erling.jdz.cv.frameworkinf.face.yunet.YuNetFrameWorkIf;
import com.erling.jdz.cv.frameworkinf.yolo.YoloFrameWorkInf;
import com.erling.jdz.load.DyLinkLibLoader;
import com.erling.jdz.load.RunEnv;
import com.erling.jdz.load.ann.ConfigPath;
import com.erling.jdz.load.ann.DyLinkLib;
import com.erling.jdz.load.ann.NoConfig;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LoaderTest {


    public LoaderTest() throws IllegalAccessException {
        RunEnv.SET_ENV.run();
        DyLinkLibLoader.load(this, YoloFrameWork.class, YoloFrameWorkInf.class);
        DyLinkLibLoader.load(this, ArcFrameWork.class, ArcFaceFrameWorkInf.class);
        DyLinkLibLoader.load(this, YuNetFrameWork.class, YuNetFrameWorkIf.class);
        DyLinkLibLoader.load(this, VecUtils.class, UtilsFrameWorkInf.class);

    }

    @DyLinkLib(path = "clibconf/lib/GeneralDnnLib_Framework_Lib.dll")
    @ConfigPath("clibconf/modelconf/yolo_config.json")
    YoloFrameWork yoloFrameWork;


    @DyLinkLib(path = "clibconf/lib/GeneralDnnLib_Framework_Lib.dll")
    @ConfigPath("clibconf/modelconf/arcface_config.json")
    ArcFrameWork arcFrameWork;

    @DyLinkLib(path = "clibconf/lib/GeneralDnnLib_Framework_Lib.dll")
    @ConfigPath("clibconf/modelconf/yunet_config.json")
    YuNetFrameWork yunetFrameWork;

    @DyLinkLib(path = "clibconf/lib/GeneralDnnLib_Framework_Lib.dll")
    @NoConfig
    VecUtils vecUtils;



    @Test
    public void testYolo() throws IOException, IllegalAccessException {
        byte[] imageBytes = Files.readAllBytes(Path.of("./image/bus.jpg"));
        YoloOutput output = new YoloOutput();
        yoloFrameWork.detect(imageBytes, output);
        yoloFrameWork.destroy();
    }

    @Test
    public void testYuNet_Arc() throws IOException {
        byte[] imageBytes = Files.readAllBytes(Path.of("./image/bus.jpg"));
        yunetFrameWork.featureVecArc(
                arcFrameWork,
                imageBytes.length, imageBytes,
                new YuNetOutput(), new FeatureOutput()
        );
    }

    @Test
    public void testFeatureVec512() throws IOException {
        byte[] imageBytes1 = Files.readAllBytes(Path.of("./image/p3.jpg"));
        byte[] imageBytes2 = Files.readAllBytes(Path.of("./image/p4.jpg"));
        YuNetOutput output = new YuNetOutput();
        FeatureOutput featureOutput1 = new FeatureOutput();
        FeatureOutput featureOutput2 = new FeatureOutput();
        yunetFrameWork.featureVecArc(
                arcFrameWork,
                imageBytes1.length, imageBytes1,
                output, featureOutput1
        );
        yunetFrameWork.featureVecArc(
                arcFrameWork,
                imageBytes2.length, imageBytes2,
                output, featureOutput2
        );
        vecUtils.CompareVec512(featureOutput1.getDataAsBytes(), featureOutput2.getDataAsBytes());
    }


}
