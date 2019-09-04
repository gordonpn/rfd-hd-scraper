package com.rfdhd.scraper.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rfdhd.scraper.model.ThreadInfo;
import org.pmw.tinylog.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GsonIO {

    private Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();

    public void write(String filepath, Map<String, ThreadInfo> newMap) {
        Map<String, ThreadInfo> currentMap = newMap;
        Map<String, ThreadInfo> mapToAppend = read(filepath, newMap);
        FileWriter fileWriter = null;

        currentMap.putAll(mapToAppend);

        try {
            fileWriter = new FileWriter(filepath);

            Logger.info("Writing " + filepath);
            gsonWriter.toJson(currentMap, fileWriter);
            Logger.info("Successfully wrote " + filepath);
            Logger.info("Size of: " + filepath + " is " + currentMap.size() + " items");

            fileWriter.close();
        } catch (IOException e) {
            Logger.error("Error with writer. | " + e.getMessage());
        }
    }

    private Map<String, ThreadInfo> read(String filepath, Map<String, ThreadInfo> newMap) {
        Map mapFromJson = new HashMap<String, ThreadInfo>();
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(filepath);

            Logger.info("Reading " + filepath);
            Type type = new TypeToken<Map<String, ThreadInfo>>() {
            }.getType();
            mapFromJson = new Gson().fromJson(fileReader, type);
            Logger.info("Succesfully read from " + filepath);

            if (mapFromJson != null) {
                Logger.info("Size of " + filepath + " is " + mapFromJson.size());
                mapFromJson.keySet().forEach(newMap::remove);
                Logger.info("Adding " + newMap.size() + " new items.");
                newMap.putAll(mapFromJson);
            }

            fileReader.close();
        } catch (FileNotFoundException e) {
            Logger.error("Could not open file. | " + e.getMessage());
            File file = new File(filepath);
            try {
                Logger.info("Creating " + filepath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException ex) {
                Logger.error("Could not create new file. | " + ex.getMessage());
            }
        } catch (IOException e) {
            Logger.error("Error with reader. | " + e.getMessage());
        }

        return newMap;
    }
}
