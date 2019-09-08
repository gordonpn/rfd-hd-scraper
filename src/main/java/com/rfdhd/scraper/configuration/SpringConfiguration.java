package com.rfdhd.scraper.configuration;

import com.rfdhd.scraper.model.NoConfigurationException;
import com.rfdhd.scraper.model.configuration.Configuration;
import org.pmw.tinylog.Logger;

public class SpringConfiguration {

    private Configuration configuration = null;

    Configuration getConfiguration() {

        if (configuration == null) {
            ConfigurationLoader configurationLoader = new ConfigurationLoader();
            try {
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
}
