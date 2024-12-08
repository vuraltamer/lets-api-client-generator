package com.lets.apis.client.generator.util;

import com.lets.apis.client.generator.model.ApiDetail;
import com.lets.apis.client.generator.model.GradleDetail;
import com.lets.apis.client.generator.properties.CallerProperties;
import com.lets.apis.client.generator.properties.PropertyReader;

public class GradleDetailUtil {

    public static void load(ApiDetail apiDetail) {
        CallerProperties callerProperties = PropertyReader.properties();
        apiDetail.setGradleDetail(GradleDetail.create(callerProperties));
        apiDetail.setDirectoryPath(new String("{API_CLIENT_PATH}.generator/{API_NAME}/")
                .replace("{API_NAME}", callerProperties.getApiName())
                .replace("{API_CLIENT_PATH}", callerProperties.getApiClientPath()));
    }
}