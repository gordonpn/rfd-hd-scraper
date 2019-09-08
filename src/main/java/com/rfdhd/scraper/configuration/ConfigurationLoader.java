package com.rfdhd.scraper.configuration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rfdhd.scraper.model.NoConfigurationException;
import com.rfdhd.scraper.model.configuration.Configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

public class ConfigurationLoader {

    public Configuration loadConfiguration() throws NoConfigurationException {
        if (isProdMachine()) {

        } else if (isTestMachine()) {

        } else {
            throw new NoConfigurationException("Cannot determine machine.");
        }
        // todo check which machine this is running on
        Configuration configuration = null;
        try (FileReader fileReader = new FileReader("/src/main/resources/configuration.json")) {
            Type type = new TypeToken<Configuration>() {
            }.getType();
            configuration = new Gson().fromJson(fileReader, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configuration;
    }

    private boolean isTestMachine() {
    }

    private boolean isProdMachine() {
    }
}
