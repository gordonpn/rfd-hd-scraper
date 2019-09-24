package com.rfdhd.scraper;

import org.pmw.tinylog.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class Testing {

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("MMM d['st']['nd']['rd']['th'], yyyy h:mm a")
                .toFormatter(Locale.ENGLISH);

        Logger.info(now.format(formatter));
    }
}
