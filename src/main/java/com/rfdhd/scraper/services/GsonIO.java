package com.rfdhd.scraper.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rfdhd.scraper.model.ThreadInfo;
import org.pmw.tinylog.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GsonIO {

    private Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();

    public void write(String filepath, Map<String, ThreadInfo> map) {
        Map<String, ThreadInfo> mapToWrite = read(filepath, map);
        FileWriter fileWriter = null;

        try {
            // todo how to create the file if it doesn't exist?
            // todo not appending properly
            fileWriter = new FileWriter(filepath, true);
        } catch (IOException e) {
            Logger.error("Error with writer. | " + e.getMessage());
        }

        Logger.info("Writing " + filepath);

        gsonWriter.toJson(mapToWrite, fileWriter);

        Logger.info("Successfully wrote " + filepath);

        try {
            fileWriter.close();
        } catch (IOException e) {
            Logger.error("Error with writer. | " + e.getMessage());
        }
    }

    private Map<String, ThreadInfo> read(String filepath, Map<String, ThreadInfo> existingMap) {
        Map mapFromJson = new HashMap<String, ThreadInfo>();
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(filepath);
        } catch (FileNotFoundException e) {
            Logger.error("Could not open file. | " + e.getMessage());
        }

        Logger.info("Reading " + filepath);

        mapFromJson = new Gson().fromJson(fileReader, mapFromJson.getClass());

        Logger.info("Succesfully read from " + filepath);

        if (mapFromJson != null) {
            mapFromJson.keySet().forEach(existingMap::remove);
        }

        try {
            fileReader.close();
        } catch (IOException e) {
            Logger.error("Error with writer. | " + e.getMessage());
        }

        return existingMap;
    }
}
