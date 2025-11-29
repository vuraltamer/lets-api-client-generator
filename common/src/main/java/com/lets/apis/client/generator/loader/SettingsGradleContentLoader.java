package com.lets.apis.client.generator.loader;

import com.lets.apis.client.generator.properties.model.CallerProperties;
import com.lets.apis.client.generator.template.TemplateReader;

public class SettingsGradleContentLoader {

    public static String load(CallerProperties callerProperties) {
        return new String(TemplateReader.content("settings-gradle"))
                .replace("{CLIENT_NAME}", callerProperties.getClientName());
    }
}