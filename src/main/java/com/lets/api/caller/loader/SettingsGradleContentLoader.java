package com.lets.api.caller.loader;

import com.lets.api.caller.properties.CallerProperties;

import static com.lets.api.caller.constants.GradleConstants.SETTINGS_GRADLE_CONTENT;

public class SettingsGradleContentLoader {

    public static String load(CallerProperties callerProperties) {
        return new String(SETTINGS_GRADLE_CONTENT)
                .replace("{API_NAME}", callerProperties.getApiName());
    }
}