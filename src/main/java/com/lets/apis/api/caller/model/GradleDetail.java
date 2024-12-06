package com.lets.apis.api.caller.model;

import com.lets.apis.api.caller.creator.SettingsGradleCreator;
import com.lets.apis.api.caller.properties.CallerProperties;
import com.lets.apis.api.caller.creator.BuildGradleCreator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GradleDetail {
    private CallerProperties callerProperties;
    private GradleModel buildGradle;
    private GradleModel settingsGradle;

    public static GradleDetail create(CallerProperties callerProperties) {
        GradleDetail gradleDetail = new GradleDetail();
        gradleDetail.setBuildGradle(BuildGradleCreator.create(callerProperties));
        gradleDetail.setSettingsGradle(SettingsGradleCreator.create(callerProperties));
        return gradleDetail;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class GradleModel {
        private String name;
        private String content;
    }
}