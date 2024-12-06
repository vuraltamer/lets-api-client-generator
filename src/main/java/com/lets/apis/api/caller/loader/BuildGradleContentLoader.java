package com.lets.apis.api.caller.loader;

import com.lets.apis.api.caller.constants.ApiConstants;
import com.lets.apis.api.caller.properties.CallerProperties;
import com.lets.apis.api.caller.constants.GradleConstants;

import java.util.List;
import java.util.stream.Collectors;

public class BuildGradleContentLoader {

    public static String load(CallerProperties callerProperties) {
        return new String(GradleConstants.BUILD_GRADLE_CONTENT)
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
                .map(dependency -> new String("\timplementation '{DEPENDENCY}'")
                        .replace("{DEPENDENCY}", dependency))
                .collect(Collectors.joining("\n"));
    }
}