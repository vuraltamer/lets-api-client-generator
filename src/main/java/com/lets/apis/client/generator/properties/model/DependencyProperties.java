package com.lets.apis.client.generator.properties.model;

import lombok.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DependencyProperties {

    private Set<Version> versions;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Version {
        private String java;
        private String springBoot;
        private String springCloud;
        private String dependencyManagement;
        private String gradle;
    }

    public Version getVersion(String javaVersion) {
        return this.getVersions().stream()
                .filter(version -> version.getJava().equals(javaVersion))
                .findAny()
                .orElse(null);
    }
}