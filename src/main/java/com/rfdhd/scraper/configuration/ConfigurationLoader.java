package com.rfdhd.scraper.configuration;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rfdhd.scraper.model.NoConfigurationException;
import com.rfdhd.scraper.model.configuration.Configuration;
import com.rfdhd.scraper.model.configuration.JsonConfiguration;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;

import static com.rfdhd.scraper.utility.MachineChecker.isProdMachine;
import static com.rfdhd.scraper.utility.MachineChecker.isTestMachine;

class ConfigurationLoader {

    private static Logger logger = (Logger) LoggerFactory.getLogger(ConfigurationLoader.class);

    Configuration loadConfiguration() throws NoConfigurationException, FileNotFoundException {
        InputStream filePath = getConfigFilePath();

        try (InputStreamReader fileReader = new InputStreamReader(filePath)) {
            Type type = new TypeToken<JsonConfiguration>() {
            }.getType();
            JsonConfiguration jsonConfiguration = new Gson().fromJson(fileReader, type);
            logger.info("Parsed configuration.json successfully.");

            if (isProdMachine()) {

                logger.info("Running on a production machine.");
                return jsonConfiguration.getProdConfiguration();

            } else if (isTestMachine()) {

                logger.info("Running on a test machine.");
                return jsonConfiguration.getTestConfiguration();

            } else {
                throw new NoConfigurationException("Cannot determine machine.");
            }
        } catch (IOException e) {
            logger.error("Error getting configuration | {}", e.getMessage());
        }

        throw new NoConfigurationException("Could not find a configuration.");
    }

    private InputStream getConfigFilePath() throws NoConfigurationException, FileNotFoundException {
        InputStream filePath;

        if (isProdMachine()) {

            filePath = new FileInputStream("./configuration.json");
            logger.info("Configuration found in: {}", filePath);
            return filePath;

        } else if (isTestMachine()) {

            filePath = new FileInputStream("src/main/resources/configuration.json");
            logger.info("Configuration found in: {}", filePath);
            return filePath;

        }

        throw new NoConfigurationException("Could not find configuration file.");
    }
}
