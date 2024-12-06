package com.lets.api.caller.loader;

import com.lets.api.caller.constants.ApiConstants;
import com.lets.api.caller.properties.CallerProperties;

import java.util.List;
import java.util.stream.Collectors;

import static com.lets.api.caller.constants.GradleConstants.BUILD_GRADLE_CONTENT;

public class BuildGradleContentLoader {

    public static String load(CallerProperties callerProperties) {
        return new String(BUILD_GRADLE_CONTENT)
                .replace("{JAR_NAME}", callerProperties.getApiName())
                        .replace("{IMPLEMENTATIONS}", getImplementations(callerProperties))
                        .replace("{JAVA_VERSION}", callerProperties.getJavaVersion());
    }

    private static String getImplementations(CallerProperties callerProperties) {
        return callerProperties.getDependencies() == null ? ApiConstants.EMPTY
                : getImplementations(callerProperties.getDependencies());
    }

    private static String getImplementations(List<String> dependencies) {
        return dependencies.stream()
                .map(dependency -> new String("implementation '{DEPENDENCY}'")
                        .replace("{DEPENDENCY}", dependency))
                .collect(Collectors.joining("\n"));
    }
}