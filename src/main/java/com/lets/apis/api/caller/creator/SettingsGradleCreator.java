package com.lets.apis.api.caller.creator;

import com.lets.apis.api.caller.loader.SettingsGradleContentLoader;
import com.lets.apis.api.caller.model.GradleDetail;
import com.lets.apis.api.caller.properties.CallerProperties;

public class SettingsGradleCreator {

    public static GradleDetail.GradleModel create(CallerProperties callerProperties) {
        GradleDetail.GradleModel settingsGradle = new GradleDetail.GradleModel();
        settingsGradle.setName("settings.gradle");
        settingsGradle.setContent(SettingsGradleContentLoader.load(callerProperties));
        return settingsGradle;
    }
}