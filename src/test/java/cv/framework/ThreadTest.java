package cv.framework;

import com.erling.jdz.cv.framework.yolo.YoloFrameWork;
import com.erling.jdz.cv.framework.yolo.YoloOutput;
import com.erling.jdz.cv.frameworkinf.yolo.YoloFrameWorkInf;
import com.erling.jdz.load.DyLinkLibLoader;
import com.erling.jdz.load.RunEnv;
import com.erling.jdz.load.ann.DyLinkLib;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ThreadTest {

    @DyLinkLib(path = "clibconf/lib/GeneralDnnLib_Framework_Lib.dll")
    public YoloFrameWork yoloFrameWork =new YoloFrameWork("clibconf/modelconf/yolo_config.json");

    public ThreadTest() throws IllegalAccessException {
        RunEnv.SET_ENV.run();
        DyLinkLibLoader.load(this, yoloFrameWork.getClass(), YoloFrameWorkInf.class);

    }


    @Test
    public void testYoloFrameWork() throws IllegalAccessException, IOException {

//        var ex1= new ThreadTest();
//        var ex2= new ThreadTest();
        byte[] imageBytes1 = Files.readAllBytes(Path.of("./image/p3.jpg"));
        byte[] imageBytes2 = Files.readAllBytes(Path.of("./image/p4.jpg"));
        var t1 = new Thread(() -> {
           try {
               var ex1= new ThreadTest();
               ex1.yoloFrameWork.detect(imageBytes1, new YoloOutput());
               ex1.yoloFrameWork.destroy();
           }catch (Exception e){
               System.out.println(e.getMessage());
           }
        });
        var t2 = new Thread(() -> {
           try {
               var ex2= new ThreadTest();
               ex2.yoloFrameWork.detect(imageBytes2, new YoloOutput());
               ex2.yoloFrameWork.destroy();
           }catch (Exception e){
               System.out.println(e.getMessage());
           }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
