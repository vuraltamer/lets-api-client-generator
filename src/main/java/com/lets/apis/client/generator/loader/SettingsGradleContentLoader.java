package com.lets.apis.client.generator.loader;

import com.lets.apis.client.generator.properties.CallerProperties;
import com.lets.apis.client.generator.constants.GradleConstants;

public class SettingsGradleContentLoader {

    public static String load(CallerProperties callerProperties) {
        return new String(GradleConstants.SETTINGS_GRADLE_CONTENT)
                .replace("{API_NAME}", callerProperties.getApiName());
    }
}