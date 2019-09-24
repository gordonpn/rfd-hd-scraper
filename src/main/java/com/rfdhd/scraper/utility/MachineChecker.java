package com.rfdhd.scraper.utility;

public class MachineChecker {

    private static String osName = System.getProperty("os.name").toLowerCase();

    public static boolean isTestMachine() {
        return osName.contains("mac") || osName.contains("windows");
    }

    public static boolean isMacMachine() {
        return osName.contains("mac");
    }

    public static boolean isWindowsMachine() {
        return osName.contains("windows");
    }

    public static boolean isProdMachine() {
        return osName.contains("linux");
    }
}
