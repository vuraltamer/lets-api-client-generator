package com.lets.api.caller.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.lets.api.caller.constants.ApiConstants.EMPTY;
import static com.lets.api.caller.util.base.Util.isEmpty;
import static java.util.Collections.EMPTY_LIST;

@Getter
@NoArgsConstructor
public class CallerProperties {

    private String javaVersion;
    private String gradleVersion;
    private String apiName;
    private String scanPackage = "";
    private List<String> dependencies;

    public static CallerProperties create(Properties properties) {
        CallerProperties callerProperties = new CallerProperties();
        callerProperties.setApiName(properties.getProperty("com.lets.api.caller.api-name"));
        callerProperties.setScanPackage(properties.getProperty("com.lets.api.caller.scan-package"));
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

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = isEmpty(javaVersion) ? "17" : javaVersion;
    }

    public void setGradleVersion(String gradleVersion) {
        this.gradleVersion = isEmpty(gradleVersion) ? "8.5" : gradleVersion;
    }

    public void setApiName(String apiName) {
        this.apiName = isEmpty(apiName) ? "sample-service-client" : apiName;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = isEmpty(scanPackage) ? "17" : scanPackage;;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = isEmpty(dependencies) ? EMPTY_LIST : dependencies;;
    }
}