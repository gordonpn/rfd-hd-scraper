package com.rfdhd.scraper.services;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rfdhd.scraper.model.ThreadInfo;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class GsonIO {

    private static Logger logger = (Logger) LoggerFactory.getLogger(GsonIO.class);

    public void move(String fileFromPath, String filePathTo) {
        File fileFrom = new File(fileFromPath);

        Map mapFrom = read(fileFromPath);
        Map mapTo = read(filePathTo);
        mapTo.putAll(mapFrom);

        logger.info("Moving contents of {} to {}", fileFromPath, filePathTo);
        write(filePathTo, mapTo);

        logger.info("Deleting: {}", fileFromPath);
        boolean deleteSuccess = fileFrom.delete();
        logger.info("Delete was {}", deleteSuccess ? "successful" : "unsuccessful");
    }

    public Map read(String filepath) {
        Map mapFromJson = new LinkedHashMap<String, ThreadInfo>();

        try (FileReader fileReader = new FileReader(filepath)) {

            logger.info("Reading {}", filepath);
            Type type = new TypeToken<Map<String, ThreadInfo>>() {
            }.getType();
            mapFromJson = new Gson().fromJson(fileReader, type);

            if (mapFromJson == null || mapFromJson.isEmpty()) {
                return new LinkedHashMap();
            }

            logger.info("Successfully read from {}", filepath);
            logger.info("Size of {} is {}", filepath, mapFromJson.size());

        } catch (FileNotFoundException e) {
            logger.error("Could not open file. | {}", e.getMessage());
            createFile(filepath);
        } catch (IOException e) {
            logger.error("Error with reader. | {}", e.getMessage());
            throw new RuntimeException(e);
        }

        return mapFromJson;
    }

    public void write(String toThisFile, Map<String, ThreadInfo> fromThisMap) {
        Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter fileWriter = new FileWriter(toThisFile)) {

            logger.info("Writing {}", toThisFile);
            gsonWriter.toJson(fromThisMap, fileWriter);
            logger.info("Successfully wrote {}", toThisFile);
            logger.info("Size of: {} is {} items", toThisFile, fromThisMap.size());

        } catch (IOException e) {
            logger.error("Error with writer. | {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void createFile(String filepath) {
        File file = new File(filepath);
        try {
            logger.info("Creating {}", filepath);
            boolean madeDirs = file.getParentFile().mkdirs();
            boolean madeFiles = file.createNewFile();
            logger.info("Creation of directories was {}", madeDirs ? "successful" : "unsuccessful");
            logger.info("Creation of file was {}", madeFiles ? "successful" : "unsuccessful");
        } catch (IOException ex) {
            logger.error("Could not create new file. | {}", ex.getMessage());
        }
    }

    public Map removeDuplicates(Map<String, ThreadInfo> mapGiven, String filePathCompareWith) {
        Map newMap = new LinkedHashMap();
        Map mapCompareWith = read(filePathCompareWith);

        if (mapGiven == null || mapGiven.isEmpty()) {
            return newMap;
        }

        logger.info("Removing duplicates when comparing with {}", filePathCompareWith);
        logger.info("Size before: {}", mapGiven.size());

        mapGiven.forEach((threadID, threadInfo) -> {
            if (mapCompareWith.get(threadID) == null) {
                newMap.put(threadID, threadInfo);
            }
        });

        logger.info("Size after: {}", newMap.size());

        return newMap;
    }
}
