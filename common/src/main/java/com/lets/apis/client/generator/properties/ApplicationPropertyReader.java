package com.lets.apis.client.generator.properties;

import com.lets.apis.client.generator.properties.model.CallerProperties;

public class ApplicationPropertyReader {

    private static CallerProperties callerProperties;

    public static CallerProperties properties() {
        return callerProperties;
    }

    public static void load(String json) {
        callerProperties = CallerProperties.create(json);
    }
}