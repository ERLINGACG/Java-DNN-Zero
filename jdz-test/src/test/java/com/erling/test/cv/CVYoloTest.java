package com.erling.test.cv;

import com.erling.core.load.ann.JdzFramework;
import com.erling.core.load.env.SetRunTimeEnv;
import com.erling.core.load.jna.DyLinkLibLoader;
import com.erling.opencv.yolo.framework.CV_YoloFrameWork;
import com.erling.opencv.yolo.framework.CV_YoloFrameWorkInf;
import com.erling.opencv.yolo.sturct.YoloOutput;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CVYoloTest {

    @JdzFramework(
            rootPath = "E:/ZeroPlan/C++/GeneralDnnLib-Zero/cmake-build-release-visual-studio/gdlz_test_lib/Release/",
            name = "GeneralDnnLib_Framework_Lib",
            mapping = CV_YoloFrameWorkInf.class,
            isConfig = true,
            configPath = "libconfig/cv_yolo_config/yolo_config.json"

    )
    CV_YoloFrameWork yoloFrameWork;

    public CVYoloTest(){
        SetRunTimeEnv.SET.run();
        DyLinkLibLoader.Load(this);
    }

    @Test
    public void Load(){

    }

    @Test
    public void Destroy(){
        yoloFrameWork.destroy();
    }

    @Test
    public void Detect() throws IOException {
        var image =Files.readAllBytes(Path.of("./libconfig/cv_yolo_config/bus.jpg"));
        System.out.println(image.length);
        var output = new YoloOutput();
        yoloFrameWork.detect(image, output);
        System.out.println(output.getJsonStr());
    }

    @Test
    public void HostDetect() throws IOException {
        var image =Files.readAllBytes(Path.of("./libconfig/cv_yolo_config/bus.jpg"));
        System.out.println(image.length);
        var output = new YoloOutput();

        for (int i = 0; i < 100; i++) {
            var startTime=System.currentTimeMillis();
            yoloFrameWork.detect(image, output);
            var endTime=System.currentTimeMillis();
            System.out.println("HostDetect time: "+(endTime-startTime)+"ms");
            System.out.println(output.getJsonStr());
        }

    }

    @Test
    public void ThreadDetect() throws IOException, CloneNotSupportedException, InterruptedException {
        var image =Files.readAllBytes(Path.of("./libconfig/cv_yolo_config/bus.jpg"));


        var thread1=new Thread(()->{
            CV_YoloFrameWork new_yoloFrameWork_1 = null;
            try {
                new_yoloFrameWork_1 = yoloFrameWork.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            var output = new YoloOutput();
            new_yoloFrameWork_1.detect(image, output);
            System.out.println(output.getJsonStr());
            new_yoloFrameWork_1.destroy();
        });

        var thread2=new Thread(()->{
            CV_YoloFrameWork new_yoloFrameWork_2 = null;
            try {
                new_yoloFrameWork_2 = yoloFrameWork.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            var output = new YoloOutput();
            new_yoloFrameWork_2.detect(image, output);
            System.out.println(output.getJsonStr());
            new_yoloFrameWork_2.destroy();
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}
