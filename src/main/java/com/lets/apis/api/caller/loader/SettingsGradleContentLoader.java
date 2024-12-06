package com.lets.apis.api.caller.loader;

import com.lets.apis.api.caller.properties.CallerProperties;
import com.lets.apis.api.caller.constants.GradleConstants;

public class SettingsGradleContentLoader {

    public static String load(CallerProperties callerProperties) {
        return new String(GradleConstants.SETTINGS_GRADLE_CONTENT)
                .replace("{API_NAME}", callerProperties.getApiName());
    }
}