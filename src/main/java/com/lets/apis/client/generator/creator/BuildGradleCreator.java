package com.lets.apis.client.generator.creator;

import com.lets.apis.client.generator.loader.BuildGradleContentLoader;
import com.lets.apis.client.generator.model.GradleDetail;
import com.lets.apis.client.generator.properties.CallerProperties;

public class BuildGradleCreator {

    public static GradleDetail.GradleModel create(CallerProperties callerProperties) {
        GradleDetail.GradleModel buildGradle = new GradleDetail.GradleModel();
        buildGradle.setName("build.gradle");
        buildGradle.setContent(BuildGradleContentLoader.load(callerProperties));
        return buildGradle;
    }
}