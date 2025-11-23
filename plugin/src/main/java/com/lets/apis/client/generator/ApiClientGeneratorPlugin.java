package com.lets.apis.client.generator;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Objects;

public class ApiClientGeneratorPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.apply(it -> it.from(
                Objects.requireNonNull(
                        getClass().getClassLoader().getResource("generator/lets-generate-api-client.gradle")
                )
        ));

        project.apply(it -> it.from(
                Objects.requireNonNull(
                        getClass().getClassLoader().getResource("generator/lets-publish-api-client.gradle")
                )
        ));
    }
}