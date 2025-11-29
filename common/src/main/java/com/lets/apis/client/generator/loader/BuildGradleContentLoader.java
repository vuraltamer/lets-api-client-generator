package com.lets.apis.client.generator.loader;

import com.lets.apis.client.generator.constants.ApiConstants;
import com.lets.apis.client.generator.properties.model.CallerProperties;
import com.lets.apis.client.generator.properties.model.DependencyProperties;
import com.lets.apis.client.generator.template.TemplateReader;
import com.lets.apis.client.generator.util.base.Util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BuildGradleContentLoader {

    public static String load(CallerProperties callerProperties, DependencyProperties dependencyProperties) {
        DependencyProperties.Version dependencyVersions = dependencyProperties.getVersion(callerProperties.getJavaVersion());
        return TemplateReader.content("build-gradle")
                .replace("{CLIENT_NAME}", callerProperties.getClientName())
                .replace("{CLIENT_VERSION}", callerProperties.getClientVersion())
                .replace("{GROUP_ID}", callerProperties.getGroupId())
                .replace("{IMPLEMENTATIONS}", getImplementations(callerProperties))
                .replace("{JAVA_VERSION}", callerProperties.getJavaVersion())
                .replace("{SPRING_FRAMEWORK_VERSION}", dependencyVersions.getSpringBoot())
                .replace("{DEPENDENCY_MANAGEMENT_VERSION}", dependencyVersions.getDependencyManagement())
                .replace("{SPRING-CLOUD-DEPENDENCIES}", dependencyVersions.getSpringCloud())
                .replace("{GRADLE_VERSION}", dependencyVersions.getGradle());
    }

    private static String getImplementations(CallerProperties callerProperties) {
        return callerProperties.getClientDependencies() == null ? ApiConstants.EMPTY
                : getImplementations(callerProperties.getClientDependencies());
    }

    private static String getImplementations(String dependencies) {
        if (Util.isEmpty(dependencies)) {
            return ApiConstants.EMPTY;
        }
        List<String> dependencyList = Arrays.asList(dependencies.split(","));
        String initRow = "implementation '{DEPENDENCY}'".replace("{DEPENDENCY}", dependencyList.get(0));
        String otherRows = dependencyList.stream()
                .skip(1)
                .map(dependency -> "\timplementation '{DEPENDENCY}'"
                        .replace("{DEPENDENCY}", dependency)
                )
                .collect(Collectors.joining("\n"));
        return initRow + (otherRows.isEmpty() ? "" : System.lineSeparator() + otherRows);
    }
}