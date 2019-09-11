package com.rfdhd.scraper.configuration;

import com.rfdhd.scraper.model.FilePaths;
import com.rfdhd.scraper.model.NoConfigurationException;
import com.rfdhd.scraper.model.configuration.Configuration;
import com.rfdhd.scraper.services.Scraper;
import org.pmw.tinylog.Logger;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class SpringConfiguration {

    private Configuration configuration = null;

    @Bean
    Configuration getConfiguration() {

        if (configuration == null) {
            ConfigurationLoader configurationLoader = new ConfigurationLoader();
            try {
                Logger.info("Loading configuration.json");
                configuration = configurationLoader.loadConfiguration();
            } catch (NoConfigurationException e) {
                Logger.error(e.getMessage());
                Logger.error("Exiting app.");
                System.exit(1);
            }
            return configuration;
        }

        return configuration;
    }

    @Bean
    FilePaths getFilePaths() {
        configuration = getConfiguration();
        String rootFolder = configuration.getRootFolder();

        FilePaths filePaths = new FilePaths();

        filePaths.setScrapingsJson(rootFolder + "scrapings.json");
        filePaths.setDailyDigestJson(rootFolder + "dailyDigest.json");
        filePaths.setArchiveJson(rootFolder + "archive.json");

        Logger.info("Setting scrapingsJson to: " + filePaths.getScrapingsJson());
        Logger.info("Setting dailyDigestJson to: " + filePaths.getDailyDigestJson());
        Logger.info("Settings archiveJson to: " + filePaths.getArchiveJson());

        return filePaths;
    }

    @Bean
    Scraper getScraper() {
        configuration = getConfiguration();

        return new Scraper(configuration.getPages());
    }

    /*spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true*/
}
