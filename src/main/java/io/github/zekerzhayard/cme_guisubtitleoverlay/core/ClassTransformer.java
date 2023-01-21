package io.github.zekerzhayard.cme_guisubtitleoverlay.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String className, String transformedName, byte[] basicClass) {
        if ("net.minecraft.client.gui.GuiSubtitleOverlay".equals(transformedName)) {
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            new ClassReader(basicClass).accept(new ClassVisitor(Opcodes.ASM5, cw) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                    if (RemapUtils.checkMethodName(className.replace('.', '/'), name, desc, "<init>") && RemapUtils.checkMethodDesc(desc, "(Lnet/minecraft/client/Minecraft;)V")) {
                        mv = new MethodVisitor(Opcodes.ASM5, mv) {
                            @Override
                            public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                                if (opcode == Opcodes.INVOKESTATIC && RemapUtils.checkClassName(owner, "com/google/common/collect/Lists") && RemapUtils.checkMethodName(owner, name, desc, "newArrayList") && RemapUtils.checkMethodDesc(desc, "()Ljava/util/ArrayList;")) {
                                    this.visitTypeInsn(Opcodes.NEW, "java/util/Vector");
                                    this.visitInsn(Opcodes.DUP);
                                    opcode = Opcodes.INVOKESPECIAL;
                                    owner = "java/util/Vector";
                                    name = "<init>";
                                    desc = "()V";
                                    itf = false;
                                }
                                super.visitMethodInsn(opcode, owner, name, desc, itf);
                            }
                        };
                    }
                    return mv;
                }
            }, ClassReader.EXPAND_FRAMES);
            basicClass = cw.toByteArray();
        }
        return basicClass;
    }
}
