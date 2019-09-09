package com.rfdhd.scraper.configuration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rfdhd.scraper.model.NoConfigurationException;
import com.rfdhd.scraper.model.configuration.Configuration;
import com.rfdhd.scraper.model.configuration.JsonConfiguration;
import org.pmw.tinylog.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

public class ConfigurationLoader {

    public Configuration loadConfiguration() throws NoConfigurationException {

        try (FileReader fileReader = new FileReader("src/main/resources/configuration.json")) {
            Type type = new TypeToken<JsonConfiguration>() {
            }.getType();
            JsonConfiguration jsonConfiguration = new Gson().fromJson(fileReader, type);
            Logger.info("Parsed configuration.json succesfully.");

            if (isProdMachine()) {

                Logger.info("Running on a production machine.");
                return jsonConfiguration.getProdConfiguration();

            } else if (isTestMachine()) {

                Logger.info("Running on a test machine.");
                return jsonConfiguration.getTestConfiguration();

            } else {
                throw new NoConfigurationException("Cannot determine machine.");
            }
        } catch (FileNotFoundException e) {
            Logger.error(e.getMessage());
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }

        throw new NoConfigurationException("Could not find a configuration.");
    }

    private boolean isTestMachine() {
        return System.getProperty("os.name").toLowerCase().contains("mac") || System.getProperty("os.name").toLowerCase().contains("windows");
    }

    private boolean isProdMachine() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }
}
