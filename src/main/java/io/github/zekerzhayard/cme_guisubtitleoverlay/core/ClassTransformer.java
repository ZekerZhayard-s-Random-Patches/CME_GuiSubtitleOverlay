package io.github.zekerzhayard.cme_guisubtitleoverlay.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class ClassTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String className, String transformedName, byte[] basicClass) {
        if ("net.minecraft.client.gui.GuiSubtitleOverlay".equals(transformedName)) {
            ClassNode cn = new ClassNode();
            new ClassReader(basicClass).accept(cn, ClassReader.EXPAND_FRAMES);
            for (MethodNode mn : cn.methods) {
                if (RemapUtils.checkMethodName(cn.name, mn.name, mn.desc, "<init>") && RemapUtils.checkMethodDesc(mn.desc, "(Lnet/minecraft/client/Minecraft;)V")) {
                    for (AbstractInsnNode ain : mn.instructions.toArray()) {
                        if (ain.getOpcode() == Opcodes.INVOKESTATIC) {
                            MethodInsnNode min = (MethodInsnNode) ain;
                            if (RemapUtils.checkClassName(min.owner, "com/google/common/collect/Lists") && RemapUtils.checkMethodName(min.owner, min.name, min.desc, "newArrayList") && RemapUtils.checkMethodDesc(min.desc, "()Ljava/util/ArrayList;")) {
                                mn.instructions.insertBefore(min, new TypeInsnNode(Opcodes.NEW, "io/github/zekerzhayard/cme_guisubtitleoverlay/CopyOnWriteArrayListWithMutableIterator"));
                                mn.instructions.insertBefore(min, new InsnNode(Opcodes.DUP));
                                mn.instructions.set(min, new MethodInsnNode(Opcodes.INVOKESPECIAL, "io/github/zekerzhayard/cme_guisubtitleoverlay/CopyOnWriteArrayListWithMutableIterator", "<init>", "()V", false));
                            }
                        }
                    }
                }
            }
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            cn.accept(cw);
            basicClass = cw.toByteArray();
        }
        return basicClass;
    }
}
