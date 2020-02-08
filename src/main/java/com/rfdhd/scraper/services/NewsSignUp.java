package com.rfdhd.scraper.services;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rfdhd.scraper.configuration.SpringConfiguration;
import com.rfdhd.scraper.model.configuration.Configuration;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class NewsSignUp {

    private static Logger logger = (Logger) LoggerFactory.getLogger(NewsSignUp.class);

    public void saveEmail(String userEmail) {
        if (isValid(userEmail)) {
            ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
            Configuration configuration = context.getBean(Configuration.class);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<String> mailingList = configuration.getMailingList();

            mailingList.add(userEmail);

            try (FileWriter writer = new FileWriter(configuration.getRootFolder() + "mailingList.json")) {
                gson.toJson(mailingList, writer);
            } catch (IOException e) {
                logger.error("Error with NewsSignUp class while writing file. | {}", e.getMessage());
            }

            configuration.setMailingList(mailingList);
        }
    }

    private boolean isValid(String userEmail) {
        EmailValidator emailValidator = EmailValidator.getInstance();

        return emailValidator.isValid(userEmail);
    }
}
