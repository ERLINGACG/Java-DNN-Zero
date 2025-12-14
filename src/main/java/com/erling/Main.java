package com.erling;

import com.erling.jdz.cv.framework.yolo.YoloFrameWork;
import com.erling.jdz.cv.framework.yolo.YoloOutput;
import com.erling.jdz.load.RunEnv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
public class Main {
    public Main(){
        RunEnv.SET_ENV.run();
    }
    void test1(){
        YoloFrameWork yoloFrameWork=new YoloFrameWork();
    }

    void test2() throws IOException {
        byte[] bytes = Files.readAllBytes(Path.of("./image/bus.jpg"));
        if(bytes.length==0){
            throw new IOException("Failed to read image file");
        }
        YoloFrameWork yoloFrameWork=new YoloFrameWork();
        YoloOutput yoloOutput=new YoloOutput();
        for(int j=0;j<10;j++){
            long start = System.currentTimeMillis();
            yoloFrameWork.detect(bytes, yoloOutput);
            long end = System.currentTimeMillis();
            System.out.println("Time cost: " + (end - start) + " ms");
        }
        Files.write(Path.of("./image/bus_out.jpg"), yoloOutput.getDataAsBytes());



    }
    public static void main(String[] args) throws IOException {
        Main main=new Main();
        main.test2();
    }
}