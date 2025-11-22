package com.lets.apis.client.generator.util;

import com.lets.apis.client.generator.model.ApiDetail;
import com.lets.apis.client.generator.model.ApiClientModelDetail;
import com.lets.apis.client.generator.creator.ControllerModelClassCreator;

import java.util.Set;
import java.util.stream.Collectors;

public class ApiClientModelUtil {

    public static void load(ApiDetail apiDetail) {
        Set<Class> apiClasses = ControllerModelClassCreator.create(apiDetail);
        Set<ApiClientModelDetail> apiClientModelDetails = apiClasses.stream()
                .map(apiClass -> ApiClientModelDetail.create(apiClass))
                .collect(Collectors.toSet());
        apiDetail.setModelDetails(apiClientModelDetails);
    }
}