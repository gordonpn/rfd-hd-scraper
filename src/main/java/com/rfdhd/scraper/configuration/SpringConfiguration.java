package com.rfdhd.scraper.configuration;

import com.rfdhd.scraper.model.FilePaths;
import com.rfdhd.scraper.model.NoConfigurationException;
import com.rfdhd.scraper.model.configuration.Configuration;
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

        // todo get rid of this hardcoding
        filePaths.setScrapingsJson(rootFolder + "scrapings.json");
        filePaths.setCurrentLinks(rootFolder + "currentLinks.json");
        filePaths.setPastLinks(rootFolder + "pastLinks.json");

        Logger.info("Setting scrapingsJson to: " + filePaths.getScrapingsJson());
        Logger.info("Setting currentLinks to: " + filePaths.getCurrentLinks());
        Logger.info("Settings pastLinks to: " + filePaths.getPastLinks());

        return filePaths;
    }
}
