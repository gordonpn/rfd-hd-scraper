package com.rfdhd.scraper;

import com.rfdhd.scraper.configuration.SpringConfiguration;
import com.rfdhd.scraper.model.FilePaths;
import com.rfdhd.scraper.model.configuration.Configuration;
import com.rfdhd.scraper.services.Scraper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Testing {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        Configuration configuration = context.getBean(Configuration.class);
        FilePaths filePaths = context.getBean(FilePaths.class);
        Scraper scraper = context.getBean(Scraper.class);
    }
}
