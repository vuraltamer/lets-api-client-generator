package com.lets.api.caller.creator;

import com.lets.api.caller.loader.SettingsGradleContentLoader;
import com.lets.api.caller.model.GradleDetail;
import com.lets.api.caller.properties.CallerProperties;

public class SettingsGradleCreator {

    public static GradleDetail.GradleModel create(CallerProperties callerProperties) {
        GradleDetail.GradleModel settingsGradle = new GradleDetail.GradleModel();
        settingsGradle.setName("settings.gradle");
        settingsGradle.setContent(SettingsGradleContentLoader.load(callerProperties));
        return settingsGradle;
    }
}