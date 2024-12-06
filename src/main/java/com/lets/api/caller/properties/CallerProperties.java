package com.lets.api.caller.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.lets.api.caller.constants.ApiConstants.EMPTY;

@Getter
@Setter
@NoArgsConstructor
public class CallerProperties {

    private String javaVersion = "17";
    private String gradleVersion = "8.5";
    private String apiName = "sample-service-client";
    private List<String> dependencies;

    public static CallerProperties create(Properties properties) {
        CallerProperties callerProperties = new CallerProperties();
        callerProperties.setApiName(properties.getProperty("com.lets.api.caller.api-name"));
        callerProperties.setJavaVersion(properties.getProperty("com.lets.api.caller.java-version"));
        callerProperties.setGradleVersion(properties.getProperty("com.lets.api.caller.gradle-version"));
        callerProperties.setDependencies(getDependencies(properties));
        return callerProperties;
    }

    private static List<String> getDependencies(Properties properties) {
        String property = properties.getProperty("com.lets.api.caller.dependencies");
        if (property == null || property.isEmpty()) {
            return null;
        }
        String formattedProperty = property
                .replace("[", EMPTY)
                .replace("]", EMPTY);
        return Arrays.asList(formattedProperty.split(","));
    }
}