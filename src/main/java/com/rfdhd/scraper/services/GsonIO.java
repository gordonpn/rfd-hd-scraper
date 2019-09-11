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

    public void add(String filepath, Map<String, ThreadInfo> fromThisMap) {
        Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();
        Map<String, ThreadInfo> currentMap = fromThisMap;
        Map<String, ThreadInfo> mapToAppend = read(filepath, fromThisMap);

        currentMap.putAll(mapToAppend);

        try (FileWriter fileWriter = new FileWriter(filepath);) {

            Logger.info("Writing " + filepath);
            gsonWriter.toJson(currentMap, fileWriter);
            Logger.info("Successfully wrote " + filepath);
            Logger.info("Size of: " + filepath + " is " + currentMap.size() + " items");

            fileWriter.close();
        } catch (IOException e) {
            Logger.error("Error with writer. | " + e.getMessage());
        }
    }

    public Map<String, ThreadInfo> read(String filepath, Map<String, ThreadInfo> newMap) {
        Map mapFromJson = new HashMap<String, ThreadInfo>();

        try (FileReader fileReader = new FileReader(filepath)) {

            Logger.info("Reading " + filepath);
            Type type = new TypeToken<Map<String, ThreadInfo>>() {
            }.getType();
            mapFromJson = new Gson().fromJson(fileReader, type);
            Logger.info("Succesfully read from " + filepath);

            if (newMap.isEmpty()) {
                newMap.putAll(mapFromJson);
                return newMap;
            }

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

    public void move(String filePathFrom, String filePathTo) {
        File fileFrom = new File(filePathFrom);

        Map<String, ThreadInfo> mapFrom = read(filePathFrom, new HashMap<String, ThreadInfo>());

        add(filePathTo, mapFrom);

        fileFrom.delete();
    }

    public Map<String, ThreadInfo> removeDuplicates(Map<String, ThreadInfo> mapGiven, String filePathCompareWith) {
        Map<String, ThreadInfo> mapCompareWith = read(filePathCompareWith, new HashMap<String, ThreadInfo>());

        mapCompareWith.keySet().forEach(mapGiven::remove);

        return mapGiven;
    }
}
