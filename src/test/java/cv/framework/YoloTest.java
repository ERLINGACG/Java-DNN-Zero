package cv.framework;

import com.erling.jdz.cv.framework.yolo.YoloFrameWork;
import com.erling.jdz.cv.framework.yolo.YoloOutput;
import com.erling.jdz.cv.frameworkinf.yolo.YoloFrameWorkInf;
import com.erling.jdz.load.DyLinkLibLoader;
import com.erling.jdz.load.RunEnv;
import com.erling.jdz.load.ann.ConfigPath;
import com.erling.jdz.load.ann.DyLinkLib;
import com.erling.jdz.load.ann.Mapping;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class YoloTest {


    @DyLinkLib(path = "clibconf/lib/GeneralDnnLib_Framework_Lib.dll")
    @ConfigPath("clibconf/modelconf/yolo_config.json")
    @Mapping(YoloFrameWorkInf.class)
    YoloFrameWork yoloFrameWork;

    public YoloTest() throws IllegalAccessException {
        RunEnv.SET_ENV.run();
        DyLinkLibLoader.load(this);
    }

    @Test
    public void Load() {
    }

    @Test
    public void testYolo() throws IOException, IllegalAccessException {
        byte[] imageBytes = Files.readAllBytes(Path.of("./image/bus.jpg"));
        YoloOutput output = new YoloOutput();
        yoloFrameWork.detect(imageBytes, output);
        System.out.println(output.getJsonStr());
        yoloFrameWork.destroy();
    }

    @Test
    public void thread_testYolo() throws IOException, IllegalAccessException, CloneNotSupportedException, InterruptedException {
        byte[] imageBytes = Files.readAllBytes(Path.of("./image/bus.jpg"));
        var new_obj_1 = (YoloFrameWork) yoloFrameWork.clone();
        var new_obj_2 = (YoloFrameWork) yoloFrameWork.clone();

        var thread_1 = new Thread(() -> {
            YoloOutput output = new YoloOutput();
            new_obj_1.detect(imageBytes, output);
            System.out.println(output.getJsonStr());
        });

        var thread_2 = new Thread(() -> {
            YoloOutput output = new YoloOutput();
            new_obj_2.detect(imageBytes, output);
            System.out.println(output.getJsonStr());
        });

        thread_1.start();
        thread_2.start();

        thread_1.join();
        thread_2.join();

        new_obj_1.destroy();
        new_obj_2.destroy();



//        new_obj_1.destroy();
    }

}
