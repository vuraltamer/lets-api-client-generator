package com.lets.apis.client.generator.creator;

import com.lets.apis.client.generator.loader.SettingsGradleContentLoader;
import com.lets.apis.client.generator.model.GradleDetail;
import com.lets.apis.client.generator.properties.CallerProperties;

public class SettingsGradleCreator {

    public static GradleDetail.GradleModel create(CallerProperties callerProperties) {
        GradleDetail.GradleModel settingsGradle = new GradleDetail.GradleModel();
        settingsGradle.setName("settings.gradle");
        settingsGradle.setContent(SettingsGradleContentLoader.load(callerProperties));
        return settingsGradle;
    }
}