package com.rfdhd.scraper.configuration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rfdhd.scraper.model.NoConfigurationException;
import com.rfdhd.scraper.model.configuration.Configuration;
import com.rfdhd.scraper.model.configuration.JsonConfiguration;
import org.pmw.tinylog.Logger;

import java.io.*;
import java.lang.reflect.Type;

import static com.rfdhd.scraper.utility.MachineChecker.isProdMachine;
import static com.rfdhd.scraper.utility.MachineChecker.isTestMachine;

public class ConfigurationLoader {


    public Configuration loadConfiguration() throws NoConfigurationException, FileNotFoundException {
        InputStream filePath = getConfigFilePath();

        try (InputStreamReader fileReader = new InputStreamReader(filePath)) {
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

    private InputStream getConfigFilePath() throws NoConfigurationException, FileNotFoundException {
        InputStream filePath;

        if (isProdMachine()) {

            filePath = getClass().getResourceAsStream("/configuration.json");
            Logger.info("Configuration found in: " + filePath);
            return filePath;

        } else if (isTestMachine()) {

            filePath = new FileInputStream("src/main/resources/configuration.json");
            Logger.info("Configuration found in: " + filePath);
            return filePath;

        }

        throw new NoConfigurationException("Could not find configuration file.");
    }
}
