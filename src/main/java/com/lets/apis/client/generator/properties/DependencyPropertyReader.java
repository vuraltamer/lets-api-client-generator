package com.lets.apis.client.generator.properties;

import com.lets.apis.client.generator.properties.model.DependencyProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DependencyPropertyReader {

    private static DependencyProperties properties;

    public static DependencyProperties properties() {
        if (properties == null) {
            return properties = read();
        }
        return properties;
    }

    public static DependencyProperties read() {
        Properties properties = getProperties("dependencies.properties");
        Set<DependencyProperties.Version> versions = IntStream.rangeClosed(11, 24)
                .mapToObj(javaVer ->
                        DependencyProperties.Version.builder()
                                .java(String.valueOf(javaVer))
                                .springBoot(properties.getProperty("com.lets.apis.client.generator.dependency.java." + javaVer + ".spring-boot"))
                                .springCloud(properties.getProperty("com.lets.apis.client.generator.dependency.java." + javaVer + ".spring-cloud"))
                                .dependencyManagement(properties.getProperty("com.lets.apis.client.generator.dependency.java." + javaVer + ".dependency-management"))
                                .gradle(properties.getProperty("com.lets.apis.client.generator.dependency.java." + javaVer + ".gradle"))
                                .build()
                )
                .filter(v -> v != null)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return new DependencyProperties(versions);
    }

    private static Properties getProperties(String propertiesFileName) {
        Properties properties = new Properties();
        try (InputStream input = DependencyProperties.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (input == null) {
                throw new IllegalArgumentException("File not found: " + propertiesFileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties", e);
        }
        return properties;
    }
}