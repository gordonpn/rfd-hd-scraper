package com.rfdhd.scraper.services;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rfdhd.scraper.configuration.SpringConfiguration;
import com.rfdhd.scraper.model.configuration.Configuration;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;

public class NewsSignUp {

    private static Logger logger = (Logger) LoggerFactory.getLogger(NewsSignUp.class);

    private NewsSignUp() {
        throw new AssertionError("The NewsSignUp methods should be accessed statically");
    }

    public static void saveEmail(String userEmail) {
        if (isValid(userEmail)) {
            HashSet<String> mailingList = read();
            mailingList.add(userEmail);
            write(mailingList);
        }
    }

    private static boolean isValid(String userEmail) {
        EmailValidator emailValidator = EmailValidator.getInstance();

        return emailValidator.isValid(userEmail);
    }

    private static HashSet<String> read() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        Configuration configuration = context.getBean(Configuration.class);

        HashSet<String> mailingList = new HashSet<>();

        try (FileReader fileReader = new FileReader(configuration.getRootFolder() + "mailingList.json")) {
            Type type = new TypeToken<HashSet<String>>() {
            }.getType();
            mailingList = new Gson().fromJson(fileReader, type);

        } catch (FileNotFoundException e) {
            logger.error("Could not open file. | {}", e.getMessage());

        } catch (IOException e) {
            logger.error("Error with reader. | {}", e.getMessage());
        }

        return mailingList;
    }

    private static void write(HashSet<String> mailingList) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        Configuration configuration = context.getBean(Configuration.class);

        Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter fileWriter = new FileWriter(configuration.getRootFolder() + "mailingList.json")) {

            gsonWriter.toJson(mailingList, fileWriter);

        } catch (IOException e) {
            logger.error("Error with writer. | {}", e.getMessage());
        }

    }
}
