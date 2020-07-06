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
 * @see <a href="https://www.jianshu.com/p/ba9347a5a05a">jianshu</a>
 * @see <a href="https://stackoverflow.com/questions/60122037/how-can-i-detect-samsung-one-ui">oneUi</a>
 */
public class ROM {
    private static final String TAG = ROM.class.getSimpleName();

    public static final String ROM_FLYME = "FLYME";
    private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";
    private static final String KEY_VERSION_EMUI = "ro.build.version.emui";
    private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
    private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";
    private static ROM ROM;

    public static ROM getROM() {
        if (ROM == null) {
            String version;
            String name = Build.MANUFACTURER;
            if (!TextUtils.isEmpty(version = getProp(KEY_VERSION_MIUI))) {
                ROM = createMIUI(name, version);
            } else if (!TextUtils.isEmpty(version = getProp(KEY_VERSION_EMUI))) {
                ROM = createEMUI(name, version);
            } else if (!TextUtils.isEmpty(version = getProp(KEY_VERSION_OPPO))) {
                ROM = createOPPO(name, version);
            } else if (!TextUtils.isEmpty(version = getProp(KEY_VERSION_VIVO))) {
                ROM = createVIVO(name, version);
            } else {
                version = Build.DISPLAY;
                if (version.toUpperCase().contains(ROM_FLYME)) {
                    ROM = createFLYME(name, version);
                } else if (isOneUiSystem()) {
                    ROM = createOneUI(name, getOneUiVersion());
                } else {
                    ROM = createOTHER(name, version);
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
    private static final int OTHER = 0;
    private int rom;
    private String name;
    private String version;

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

    public static ROM createMIUI(String name, String version) {
        return new ROM(MIUI, name, version);
    }

    public static ROM createEMUI(String name, String version) {
        return new ROM(EMUI, name, version);
    }

    public static ROM createOPPO(String name, String version) {
        return new ROM(OPPO, name, version);
    }

    public static ROM createVIVO(String name, String version) {
        return new ROM(VIVO, name, version);
    }

    public static ROM createFLYME(String name, String version) {
        return new ROM(FLYME, name, version);
    }

    public static ROM createOneUI(String name, String version) {
        return new ROM(ONEUI, name, version);
    }

    public static ROM createOTHER(String name, String version) {
        return new ROM(OTHER, name, version);
    }

    public boolean check(int rom) {
        return getROM().rom == rom;
    }

    public boolean check(String name) {
        return name.equalsIgnoreCase(getROM().name);
    }

    public boolean isEmui() { return check(EMUI); }

    public boolean isMiui() { return check(MIUI); }

    public boolean isVivo() { return check(VIVO); }

    public boolean isOppo() { return check(OPPO); }

    public boolean isFlyme() { return check(FLYME); }

    public boolean isOneUi() {
        return check(ONEUI);
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
    private static String getOneUiVersion() {
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
