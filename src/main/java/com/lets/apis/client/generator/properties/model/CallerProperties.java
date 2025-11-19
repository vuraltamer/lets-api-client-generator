package com.lets.apis.client.generator.properties.model;

import com.lets.apis.client.generator.constants.ApiConstants;
import com.lets.apis.client.generator.util.base.Util;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static java.util.Collections.EMPTY_LIST;

@Getter
@NoArgsConstructor
public class CallerProperties {

    private String javaVersion;
    private String apiName;
    private String apiVersion;
    private String scanPackage;
    private String apiClientPath;
    private List<String> dependencies;

    public static CallerProperties create(Properties properties) {
        CallerProperties callerProperties = new CallerProperties();
        callerProperties.setApiName(properties.getProperty("com.lets.apis.client.generator.api-name"));
        callerProperties.setApiVersion(properties.getProperty("com.lets.apis.client.generator.api-version"));
        callerProperties.setScanPackage(properties.getProperty("com.lets.apis.client.generator.scan-package"));
        callerProperties.setApiClientPath(properties.getProperty("com.lets.apis.client.generator.api-client-path"));
        callerProperties.setJavaVersion(properties.getProperty("com.lets.apis.client.generator.java-version"));
        callerProperties.setDependencies(getDependencies(properties));
        return callerProperties;
    }

    private void setApiClientPath(String apiClientPath) {
        if (Util.isEmpty(apiClientPath)) {
            this.apiClientPath = "";
        }else if (apiClientPath.endsWith("/")) {
            this.apiClientPath = apiClientPath;
        } else {
            this.apiClientPath = apiClientPath + "/";
        }
    }

    private static List<String> getDependencies(Properties properties) {
        String property = properties.getProperty("com.lets.apis.client.generator.dependencies");
        if (property == null || property.isEmpty() || property.equals("[]")) {
            return null;
        }
        String formattedProperty = property
                .replace("[", ApiConstants.EMPTY)
                .replace("]", ApiConstants.EMPTY);
        return Arrays.asList(formattedProperty.split(","));
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = Util.isEmpty(javaVersion) ? "17" : javaVersion;
    }

    public void setApiName(String apiName) {
        this.apiName = Util.isEmpty(apiName) ? "sample-service-client" : apiName;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = Util.isEmpty(scanPackage) ? "" : scanPackage;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = Util.isEmpty(dependencies) ? EMPTY_LIST : dependencies;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = Util.isEmpty(apiVersion) ? "1.0.0" : apiVersion;
    }
}