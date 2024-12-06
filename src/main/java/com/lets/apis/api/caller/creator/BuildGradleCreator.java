package com.lets.apis.api.caller.creator;

import com.lets.apis.api.caller.loader.BuildGradleContentLoader;
import com.lets.apis.api.caller.model.GradleDetail;
import com.lets.apis.api.caller.properties.CallerProperties;

public class BuildGradleCreator {

    public static GradleDetail.GradleModel create(CallerProperties callerProperties) {
        GradleDetail.GradleModel buildGradle = new GradleDetail.GradleModel();
        buildGradle.setName("build.gradle");
        buildGradle.setContent(BuildGradleContentLoader.load(callerProperties));
        return buildGradle;
    }
}