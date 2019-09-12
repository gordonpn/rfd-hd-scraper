package com.rfdhd.scraper.configuration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rfdhd.scraper.model.NoConfigurationException;
import com.rfdhd.scraper.model.configuration.Configuration;
import com.rfdhd.scraper.model.configuration.JsonConfiguration;
import org.pmw.tinylog.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

public class ConfigurationLoader {

    private String osName = System.getProperty("os.name");

    public Configuration loadConfiguration() throws NoConfigurationException {
        String filePath = getConfigFilePath();

        try (FileReader fileReader = new FileReader(filePath)) {
            Type type = new TypeToken<JsonConfiguration>() {
            }.getType();
            JsonConfiguration jsonConfiguration = new Gson().fromJson(fileReader, type);
            Logger.info("Parsed configuration.json successfully.");

            if (isProdMachine()) {

                Logger.info("Running on a production machine.");
                return jsonConfiguration.getProdConfiguration();

            } else if (isTestMachine()) {

                Logger.info("Running on a test machine.");
                return jsonConfiguration.getTestConfiguration();

            } else {
                throw new NoConfigurationException("Cannot determine machine.");
            }
        } catch (IOException e) {
            Logger.error("Error getting configuration | " + e.getMessage());
        }

        throw new NoConfigurationException("Could not find a configuration.");
    }

    private String getConfigFilePath() throws NoConfigurationException {
        String filePath;

        if (isProdMachine()) {

            filePath = "./configuration.json";
            Logger.info("Configuration found in: " + filePath);
            return filePath;

        } else if (isTestMachine()) {

            filePath = "src/main/resources/configuration.json";
            Logger.info("Configuration found in: " + filePath);
            return filePath;

        }

        throw new NoConfigurationException("Could not find configuration file.");
    }

    private boolean isTestMachine() {
        return osName.toLowerCase().contains("mac") || osName.toLowerCase().contains("windows");
    }

    private boolean isProdMachine() {
        return osName.toLowerCase().contains("linux");
    }
}
