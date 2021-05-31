package com.github.liaoheng.common.util;

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

/**
 * @author liaoheng
 * @version 2018-05-30 11:40
 * @see <a href="https://www.jianshu.com/p/ba9347a5a05a">base</a>
 * @see <a href="https://stackoverflow.com/questions/60122037/how-can-i-detect-samsung-one-ui">OneUI</a>
 * @see <a href="https://github.com/SenhLinsh/Android-ROM-Identifier/blob/master/rom/src/main/java/com/linsh/rom/BuildPropKeyList.java">ColorOS & FuntouchOS</a>
 */
public class ROM {
    private static final String TAG = ROM.class.getSimpleName();

    private static final String ROM_FLYME = "FLYME";
    private static final String ROM_MOTOROLA = "MOTOROLA";
    private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";
    private static final String KEY_VERSION_EMUI = "ro.build.version.emui";
    private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
    private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";
    private static final String KEY_VERSION_COLOROS = "ro.rom.different.version";
    private static final String KEY_VERSION_FUNTOUCHOS = "ro.vivo.os.version";

    private static ROM ROM;

    public static ROM getROM() {
        if (ROM == null) {
            String version;
            String name = Build.MANUFACTURER;
            if (!TextUtils.isEmpty(version = getProp(KEY_VERSION_MIUI))) {
                ROM = new ROM(MIUI, name, version);
            } else if (!TextUtils.isEmpty(version = getProp(KEY_VERSION_EMUI))) {
                ROM = new ROM(EMUI, name, version);
            } else if (!TextUtils.isEmpty(version = getProp(KEY_VERSION_OPPO))) {
                ROM = new ROM(OPPO, name, version);
            } else if (!TextUtils.isEmpty(version = getProp(KEY_VERSION_VIVO))) {
                ROM = new ROM(VIVO, name, version);
            } else if (!TextUtils.isEmpty(version = getProp(KEY_VERSION_COLOROS))) {
                ROM = new ROM(COLOROS, name, version);
            } else if (!TextUtils.isEmpty(version = getProp(KEY_VERSION_FUNTOUCHOS))) {
                ROM = new ROM(FUNTOUCHOS, name, version);
            } else {
                version = Build.DISPLAY;
                if (version.toUpperCase().contains(ROM_FLYME)) {
                    ROM = new ROM(FLYME, name, version);
                } else if (isOneUiSystem()) {
                    ROM = new ROM(ONEUI, name, version);
                } else if (name.toUpperCase().contains(ROM_MOTOROLA) || Build.BRAND.toUpperCase().contains(ROM_MOTOROLA)) {
                    ROM = new ROM(MOTOROLA, name, version);
                } else {
                    ROM = new ROM(OTHER, name, version);
                }
            }
            L.alog().d(TAG, "getROM : " + ROM);
        }
        return ROM;
    }

    public static String getProp(String name) {
        try {
            Process p = Runtime.getRuntime().exec("getprop " + name);
            try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024)) {
                String line = input.readLine();
                input.close();
                return line;
            }
        } catch (IOException ex) {
            L.alog().e(TAG, "Unable to read prop " + name, ex);
        }
        return null;
    }

    private static final int MIUI = 1;
    private static final int EMUI = 2;
    private static final int OPPO = 3;
    private static final int VIVO = 4;
    private static final int FLYME = 5;
    private static final int ONEUI = 6;
    private static final int COLOROS = 7;
    private static final int FUNTOUCHOS = 8;
    private static final int MOTOROLA = 9;
    private static final int OTHER = 0;
    private final int rom;
    private final String name;
    private final String version;

    public ROM(int rom, String name, String version) {
        this.rom = rom;
        this.name = name;
        this.version = version;
    }

    public int getRom() {
        return rom;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    @NonNull
    @Override
    public String toString() {
        return "ROM{" +
                "rom=" + rom +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    public boolean check(int rom) {
        return getROM().rom == rom;
    }

    public boolean isEmui() {
        return check(EMUI);
    }

    public boolean isMiui() {
        return check(MIUI);
    }

    public boolean isVivo() {
        return check(VIVO);
    }

    public boolean isOppo() {
        return check(OPPO);
    }

    public boolean isFlyme() {
        return check(FLYME);
    }

    public boolean isOneUi() {
        return check(ONEUI);
    }

    public boolean isColorOS() {
        return check(COLOROS);
    }

    public boolean isFuntouchOS() {
        return check(FUNTOUCHOS);
    }

    public boolean isMotorola() {
        return check(MOTOROLA);
    }

    private static boolean isOneUiSystem() {
        try {
            return getSemPlatform() > 90000;
        } catch (Throwable ignored) {
        }
        return false;
    }

    private static int getSemPlatform() throws Throwable {
        Field semPlatformIntField = Build.VERSION.class.getDeclaredField("SEM_PLATFORM_INT");
        return semPlatformIntField.getInt(null);
    }

    //https://stackoverflow.com/questions/60122037/how-can-i-detect-samsung-one-ui
    public static String getOneUiVersion() {
        try {
            int fieldInt = getSemPlatform();
            int version = fieldInt - 90000;
            if (version < 0) {
                // not one ui (could be previous Samsung OS)
                return "";
            }
            return (version / 10000) + "." + ((version % 10000) / 100);
        } catch (Throwable ignored) {
        }
        return "";
    }
}
