package com.lets.apis.client.generator.util;

import com.lets.apis.client.generator.model.ApiDetail;
import com.lets.apis.client.generator.model.GradleDetail;
import com.lets.apis.client.generator.properties.DependencyPropertyReader;
import com.lets.apis.client.generator.properties.model.CallerProperties;
import com.lets.apis.client.generator.properties.ApplicationPropertyReader;
import com.lets.apis.client.generator.properties.model.DependencyProperties;

public class GradleDetailUtil {

    public static void load(ApiDetail apiDetail) {
        CallerProperties callerProperties = ApplicationPropertyReader.properties();
        DependencyProperties dependencyProperties = DependencyPropertyReader.properties();
        apiDetail.setGradleDetail(GradleDetail.create(callerProperties, dependencyProperties));
        apiDetail.setDirectoryPath(new String("{API_CLIENT_PATH}/.generator/{API_NAME}/")
                .replace("{API_NAME}", callerProperties.getClientName())
                .replace("{API_CLIENT_PATH}", callerProperties.getClientPath()));
    }
}