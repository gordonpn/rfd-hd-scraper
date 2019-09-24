package com.rfdhd.scraper.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rfdhd.scraper.model.ThreadInfo;
import org.pmw.tinylog.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class GsonIO {

    public void write(String toThisFile, Map<String, ThreadInfo> fromThisMap) {
        Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter fileWriter = new FileWriter(toThisFile)) {

            Logger.info("Writing " + toThisFile);
            gsonWriter.toJson(fromThisMap, fileWriter);
            Logger.info("Successfully wrote " + toThisFile);
            Logger.info("Size of: " + toThisFile + " is " + fromThisMap.size() + " items");

        } catch (IOException e) {
            Logger.error("Error with writer. | " + e.getMessage());
        }
    }

    public Map read(String filepath) {
        Map mapFromJson = new LinkedHashMap<String, ThreadInfo>();

        try (FileReader fileReader = new FileReader(filepath)) {

            Logger.info("Reading " + filepath);
            Type type = new TypeToken<Map<String, ThreadInfo>>() {
            }.getType();
            mapFromJson = new Gson().fromJson(fileReader, type);

            if (mapFromJson == null || mapFromJson.isEmpty()) {
                return new LinkedHashMap();
            }

            Logger.info("Successfully read from " + filepath);
            Logger.info("Size of " + filepath + " is " + mapFromJson.size());

        } catch (FileNotFoundException e) {
            Logger.error("Could not open file. | " + e.getMessage());
            createFile(filepath);
        } catch (IOException e) {
            Logger.error("Error with reader. | " + e.getMessage());
        }

        return mapFromJson;
    }

    public void move(String fileFromPath, String filePathTo) {
        File fileFrom = new File(fileFromPath);

        Map mapFrom = read(fileFromPath);

        Logger.info("Moving contents of " + fileFromPath + " to " + filePathTo);
        write(filePathTo, mapFrom);

        Logger.info("Deleting: " + fileFromPath);
        fileFrom.delete();
    }

    public Map<String, ThreadInfo> removeDuplicates(Map<String, ThreadInfo> mapGiven, String filePathCompareWith) {
        Map mapCompareWith = read(filePathCompareWith);

        if (!mapCompareWith.isEmpty() && !mapGiven.isEmpty()) {
            Logger.info("Remove duplicates found in map when comparing with " + filePathCompareWith);
            mapCompareWith.keySet().forEach(mapGiven::remove);
        }

        return mapGiven;
    }

    private void createFile(String filepath) {
        File file = new File(filepath);
        try {
            Logger.info("Creating " + filepath);
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException ex) {
            Logger.error("Could not create new file. | " + ex.getMessage());
        }
    }
}
