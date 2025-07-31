package com.lets.apis.client.generator.properties;

import com.lets.apis.client.generator.properties.model.CallerProperties;

import java.io.*;
import java.util.*;

public class ApplicationPropertyReader {

    private static CallerProperties callerProperties;

    public static CallerProperties properties() {
        if (callerProperties == null) {
            return callerProperties = read();
        }
        return callerProperties;
    }

    private static CallerProperties read() {
        Properties properties = getProperties();
        validateProperties(properties);
        return CallerProperties.create(properties);
    }

    private static void validateProperties(Properties properties) {
        boolean invalidStatus = properties.entrySet().stream()
                .anyMatch(prop -> prop.getValue().toString().contains("'"));
        if (invalidStatus) {
            throw new RuntimeException("CallerProperties::read:validation:error::Do not use single quotation!");
        }
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/caller-config.properties")) {
            properties.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("CallerProperties::read::exception", e);
        }
        return properties;
    }
}