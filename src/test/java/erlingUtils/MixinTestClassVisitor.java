package erlingUtils;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class MixinTestClassVisitor extends ClassVisitor {

    // 目标类的全限定名（包名+类名）
    private static final String TARGET_CLASS = "erlingUtils/ExampleA";
    // 目标方法名
    private static final String TARGET_METHOD = "a";
    // 目标方法的描述符（无参无返回值）
    private static final String TARGET_METHOD_DESC = "()V";

    protected MixinTestClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM9, cv); // 使用ASM9版本
    }

    public MixinTestClassVisitor() {
        super(Opcodes.ASM9, null); // 父类构造需要ClassVisitor，传null即可（不影响mixin方法）
    }

   public void mixin(){
        System.out.println("mixin to first");
   }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

        // 判断是否是目标方法：ExampleA的a()方法
        if (TARGET_METHOD.equals(name) && TARGET_METHOD_DESC.equals(descriptor)) {
            // 返回自定义的MethodVisitor，用于插入mixin调用
            return new MixinMethodVisitor(mv);
        }
        return mv;
    }

    private static class MixinMethodVisitor extends MethodVisitor {

        public MixinMethodVisitor(MethodVisitor mv) {
            super(Opcodes.ASM9, mv);
        }

        // 当访问方法的代码开始时调用（方法体的最开头）
        @Override
        public void visitCode() {
            // 步骤1：创建MixinTestClassVisitor实例（new指令）
            visitTypeInsn(Opcodes.NEW, "erlingUtils/MixinTestClassVisitor"); // 新建对象
            visitInsn(Opcodes.DUP); // 复制对象引用（用于构造方法调用）
            // 调用无参构造方法（构造方法名固定为<init>，描述符()V）
            visitMethodInsn(
                    Opcodes.INVOKESPECIAL,
                    "erlingUtils/MixinTestClassVisitor",
                    "<init>",
                    "()V",
                    false
            );

            // 步骤2：调用实例方法mixin()（使用INVOKEVIRTUAL指令）
            visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,  // 实例方法调用指令
                    "erlingUtils/MixinTestClassVisitor",
                    "mixin",                // 非静态方法名
                    "()V",
                    false
            );

            super.visitCode(); // 执行原a()方法逻辑
        }
    }



}
