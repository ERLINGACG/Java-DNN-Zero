package erlingUtils;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;

public class ASMTest {
    @Test
    public  void Run_test() throws IOException {
        // 1. 读取原ExampleA.class文件的字节码
        File originalClassFile = new File("build/classes/java/test/erlingUtils/ExampleA.class"); // 替换为实际路径
        byte[] originalBytes = Files.readAllBytes(originalClassFile.toPath());

        // 2. 创建ClassReader读取字节码，ClassWriter写入修改后的字节码
        ClassReader cr = new ClassReader(originalBytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        // 3. 使用自定义的ClassVisitor处理字节码
        MixinTestClassVisitor cv = new MixinTestClassVisitor(cw);
        cr.accept(cv, ClassReader.EXPAND_FRAMES);

        // 4. 获取修改后的字节码并写入文件
        byte[] modifiedBytes = cw.toByteArray();
        try (FileOutputStream fos = new FileOutputStream(originalClassFile)) {
            fos.write(modifiedBytes);
        }

        System.out.println("asm modify success");

        // 测试：加载修改后的类并调用a()方法
        try {
            Class<?> exampleAClass = Class.forName("erlingUtils.ExampleA");
            Object instance = exampleAClass.getDeclaredConstructor().newInstance();
            exampleAClass.getMethod("a").invoke(instance);
            // 输出结果：
            // mixin
            // a
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
