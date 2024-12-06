package com.lets.api.caller.util;

import com.lets.api.caller.model.ApiDetail;
import com.lets.api.caller.model.GradleDetail;
import com.lets.api.caller.properties.CallerProperties;
import com.lets.api.caller.properties.PropertyReader;

import static com.lets.api.caller.util.base.Util.isStatic;

public class GradleDetailUtil {

    public static void load(ApiDetail apiDetail) {
        CallerProperties callerProperties = PropertyReader.read();
        apiDetail.setGradleDetail(GradleDetail.create(callerProperties));
        apiDetail.setDirectoryPath(new String(".generator/{API_NAME}/")
                .replace("{API_NAME}", callerProperties.getApiName()));
    }
}