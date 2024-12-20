package com.lets.apis.client.generator.loader;

import com.lets.apis.client.generator.constants.ApiConstants;
import com.lets.apis.client.generator.properties.CallerProperties;
import com.lets.apis.client.generator.template.TemplateReader;

import java.util.List;
import java.util.stream.Collectors;

public class BuildGradleContentLoader {

    public static String load(CallerProperties callerProperties) {
        return new String(TemplateReader.content("build-gradle"))
                .replace("{JAR_NAME}", callerProperties.getApiName())
                .replace("{API_VERSION}", callerProperties.getApiVersion())
                .replace("{IMPLEMENTATIONS}", getImplementations(callerProperties))
                .replace("{JAVA_VERSION}", callerProperties.getJavaVersion())
                .replace("{GRADLE_VERSION}", callerProperties.getGradleVersion());
    }

    private static String getImplementations(CallerProperties callerProperties) {
        return callerProperties.getDependencies() == null ? ApiConstants.EMPTY
                : getImplementations(callerProperties.getDependencies());
    }

    private static String getImplementations(List<String> dependencies) {
        return dependencies.stream()
                .map(dependency -> new String("\timplementation '{DEPENDENCY}'")
                        .replace("{DEPENDENCY}", dependency))
                .collect(Collectors.joining("\n"));
    }
}