package io.github.zekerzhayard.cme_guisubtitleoverlay.core;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.commons.Remapper;

public class RemapUtils {
    public static boolean isDevelopmentEnvironment = false;
    private final static Remapper remapper = FMLDeobfuscatingRemapper.INSTANCE;

    public static String mapClassName(String name) {
        return remapper.mapType(name);
    }

    public static String mapFieldName(String owner, String name, String desc) {
        return remapper.mapFieldName(owner, name, desc);
    }

    public static String mapFieldDesc(String desc) {
        return remapper.mapDesc(desc);
    }

    public static String mapMethodName(String owner, String name, String desc) {
        return remapper.mapMethodName(owner, name, desc);
    }

    public static String mapMethodDesc(String desc) {
        return remapper.mapMethodDesc(desc);
    }

    public static boolean checkClassName(String name, String deobfName) {
        if (isDevelopmentEnvironment) {
            return name.equals(deobfName);
        } else {
            return mapClassName(name).equals(deobfName);
        }
    }

    public static boolean checkFieldName(String owner, String name, String desc, String srgName) {
        if (isDevelopmentEnvironment) {
            return mapFieldName(owner, srgName, desc).equals(name);
        } else {
            return mapFieldName(owner, name, desc).equals(srgName);
        }
    }

    public static boolean checkFieldDesc(String desc, String deobfDesc)  {
        if (isDevelopmentEnvironment) {
            return desc.equals(deobfDesc);
        } else {
            return mapFieldDesc(desc).equals(deobfDesc);
        }
    }

    public static boolean checkMethodName(String owner, String name, String desc, String srgName) {
        if (isDevelopmentEnvironment) {
            return mapMethodName(owner, srgName, desc).equals(name);
        } else {
            return mapMethodName(owner, name, desc).equals(srgName);
        }
    }

    public static boolean checkMethodDesc(String desc, String deobfDesc)  {
        if (isDevelopmentEnvironment) {
            return desc.equals(deobfDesc);
        } else {
            return mapMethodDesc(desc).equals(deobfDesc);
        }
    }
}
