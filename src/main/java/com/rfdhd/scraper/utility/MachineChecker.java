package com.rfdhd.scraper.utility;

public class MachineChecker {

    private static String osName = System.getProperty("os.name");

    public static boolean isTestMachine() {
        return osName.toLowerCase().contains("mac") || osName.toLowerCase().contains("windows");
    }

    public static boolean isProdMachine() {
        return osName.toLowerCase().contains("linux");
    }
}
