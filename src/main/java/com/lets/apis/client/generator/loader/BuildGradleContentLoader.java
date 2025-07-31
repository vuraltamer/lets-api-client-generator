package com.lets.apis.client.generator.loader;

import com.lets.apis.client.generator.constants.ApiConstants;
import com.lets.apis.client.generator.properties.model.CallerProperties;
import com.lets.apis.client.generator.properties.model.DependencyProperties;
import com.lets.apis.client.generator.template.TemplateReader;

import java.util.List;
import java.util.stream.Collectors;

public class BuildGradleContentLoader {

    public static String load(CallerProperties callerProperties, DependencyProperties dependencyProperties) {
        DependencyProperties.Version dependencyVersions = dependencyProperties.getVersion(callerProperties.getJavaVersion());
        return TemplateReader.content("build-gradle")
                .replace("{JAR_NAME}", callerProperties.getApiName())
                .replace("{API_VERSION}", callerProperties.getApiVersion())
                .replace("{IMPLEMENTATIONS}", getImplementations(callerProperties))
                .replace("{JAVA_VERSION}", callerProperties.getJavaVersion())
                .replace("{SPRING_FRAMEWORK_VERSION}", dependencyVersions.getSpringBoot())
                .replace("{DEPENDENCY_MANAGEMENT_VERSION}", dependencyVersions.getDependencyManagement())
                .replace("{SPRING-CLOUD-DEPENDENCIES}", dependencyVersions.getSpringCloud())
                .replace("{GRADLE_VERSION}", dependencyVersions.getGradle());
    }

    private static String getImplementations(CallerProperties callerProperties) {
        return callerProperties.getDependencies() == null ? ApiConstants.EMPTY
                : getImplementations(callerProperties.getDependencies());
    }

    private static String getImplementations(List<String> dependencies) {
        return dependencies.stream()
                .map(dependency -> "\timplementation '{DEPENDENCY}'"
                                        .replace("{DEPENDENCY}", dependency)
                )
                .collect(Collectors.joining("\n"));
    }
}