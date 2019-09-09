package com.rfdhd.scraper.configuration;

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
            // why is this null?
            return configuration;
        }

        return configuration;
    }
}
