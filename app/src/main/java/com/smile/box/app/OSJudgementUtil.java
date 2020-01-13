package com.smile.box.app;

public final class OSJudgementUtil {

    public static boolean isHuaWei() {
        return "HUAWEI".equalsIgnoreCase(android.os.Build.MANUFACTURER);
    }

    public static boolean isZTE() {
        return "ZTE".equalsIgnoreCase(android.os.Build.MANUFACTURER);
    }

    public static boolean is360() {
        return "360".equalsIgnoreCase(android.os.Build.MANUFACTURER) || "QIKU".equalsIgnoreCase(android.os.Build.MANUFACTURER);
    }

    public static boolean isMeizu() {
        return "Meizu".equalsIgnoreCase(android.os.Build.MANUFACTURER);
    }

    public static boolean isOPPO() {
        return "OPPO".equalsIgnoreCase(android.os.Build.MANUFACTURER);
    }

    public static boolean isVIVO() {
        return "VIVO".equalsIgnoreCase(android.os.Build.MANUFACTURER);
    }

    public static boolean isXiaoMi() {
        return "Xiaomi".equalsIgnoreCase(android.os.Build.MANUFACTURER);
    }

    public static boolean isGoogle() {
        return "Google".equalsIgnoreCase(android.os.Build.MANUFACTURER);
    }
}

