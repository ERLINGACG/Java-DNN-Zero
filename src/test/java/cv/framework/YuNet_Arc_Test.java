package cv.framework;

import com.erling.jdz.cv.framework.face.arc.ArcFrameWork;
import com.erling.jdz.cv.framework.face.feature.FeatureOutput;
import com.erling.jdz.cv.framework.face.utlis.VecUtils;
import com.erling.jdz.cv.framework.face.yunet.YuNetFrameWork;
import com.erling.jdz.cv.framework.face.yunet.YuNetOutput;
import com.erling.jdz.cv.frameworkinf.face.arc.ArcFaceFrameWorkInf;
import com.erling.jdz.cv.frameworkinf.face.utlis.UtilsFrameWorkInf;
import com.erling.jdz.cv.frameworkinf.face.yunet.YuNetFrameWorkIf;
import com.erling.jdz.load.DyLinkLibLoader;
import com.erling.jdz.load.RunEnv;
import com.erling.jdz.load.ann.ConfigPath;
import com.erling.jdz.load.ann.DyLinkLib;
import com.erling.jdz.load.ann.Mapping;
import com.erling.jdz.load.ann.NoConfig;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class YuNet_Arc_Test {

    @DyLinkLib(path = "clibconf/lib/GeneralDnnLib_Framework_Lib.dll")
    @ConfigPath("clibconf/modelconf/arcface_config.json")
    @Mapping(ArcFaceFrameWorkInf.class)
    ArcFrameWork arcFrameWork;

    @DyLinkLib(path = "clibconf/lib/GeneralDnnLib_Framework_Lib.dll")
    @ConfigPath("clibconf/modelconf/yunet_config.json")
    @Mapping(YuNetFrameWorkIf.class)
    YuNetFrameWork yunetFrameWork;

    @DyLinkLib(path = "clibconf/lib/GeneralDnnLib_Framework_Lib.dll")
    @NoConfig
    @Mapping(UtilsFrameWorkInf.class)
    VecUtils vecUtils;

    public YuNet_Arc_Test() throws IOException, IllegalAccessException {
        RunEnv.SET_ENV.run();
        DyLinkLibLoader.load(this);

    }

    @Test
    public void load(){

    }

    @Test
    public void testYuNet_Arc() throws IOException {
        byte[] imageBytes = Files.readAllBytes(Path.of("./image/kb.jpg"));
        var output0 = new YuNetOutput();
        var output1 = new FeatureOutput();
        yunetFrameWork.featureVecArc(
                arcFrameWork,
                imageBytes.length, imageBytes,
                output0, output1
        );
        vecUtils.CompareVec512(output1.getDataAsBytes(),output1.getDataAsBytes());


    }

    @Test
    public void testClone() throws CloneNotSupportedException, IOException {
        byte[] imageBytes = Files.readAllBytes(Path.of("./image/kb.jpg"));
        var cloneArcFrameWork_1 = arcFrameWork.clone();
        var cloneArcFrameWork_2 = arcFrameWork.clone();

        var cloneYuNetFrameWork_1 = yunetFrameWork.clone();
        var cloneYuNetFrameWork_2 = yunetFrameWork.clone();

        var thread_1 = new Thread(() -> {
             var output0 = new YuNetOutput();
             var output1 = new FeatureOutput();
             cloneYuNetFrameWork_1.featureVecArc(
                     cloneArcFrameWork_1,
                     imageBytes.length, imageBytes,
                     output0, output1
             );
             vecUtils.CompareVec512(output1.getDataAsBytes(),output1.getDataAsBytes());
        });

        var thread_2 = new Thread(() -> {
             var output0 = new YuNetOutput();
             var output1 = new FeatureOutput();
             cloneYuNetFrameWork_2.featureVecArc(
                     cloneArcFrameWork_2,
                     imageBytes.length, imageBytes,
                     output0, output1
             );
             vecUtils.CompareVec512(output1.getDataAsBytes(),output1.getDataAsBytes());
        });

        thread_1.start();
        thread_2.start();

        try {
            thread_1.join();
            thread_2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {

             cloneArcFrameWork_1.destroy();
             cloneArcFrameWork_2.destroy();

             cloneYuNetFrameWork_1.destroy();
             cloneYuNetFrameWork_2.destroy();
        }
    }
}
