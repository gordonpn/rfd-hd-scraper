package com.rfdhd.scraper.configuration;

import ch.qos.logback.classic.Level;

import static com.rfdhd.scraper.utility.MachineChecker.isProdMachine;

public class LoggerConfiguration {

    public static Level getLoggerLevel() {
        if (isProdMachine()) {
            return Level.INFO;
        } else {
            return Level.DEBUG;
        }
    }
}
