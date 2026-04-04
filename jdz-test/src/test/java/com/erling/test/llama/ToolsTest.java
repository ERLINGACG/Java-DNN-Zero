package com.erling.test.llama;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ToolsTest {

    @Test
    public void testTools() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("powerShell", "-c", "ls");
        // 可选：设置工作目录
        // pb.directory(new File("/path/to/working/dir"));
        // 可选：合并错误流，方便统一读取输出
        // pb.redirectErrorStream(true);

        Process process = pb.start();

        // 读取标准输出流
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), "GBK")
        );
        StringBuilder line_buffer = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
            line_buffer.append(line);
            line_buffer.append("\n");
        }
        System.out.println(line_buffer);
        int exitCode = process.waitFor();
        System.out.println("退出码：" + exitCode);
    }
}
