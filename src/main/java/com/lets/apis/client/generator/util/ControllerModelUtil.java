package com.lets.apis.client.generator.util;

import com.lets.apis.client.generator.model.ApiDetail;
import com.lets.apis.client.generator.model.ControllerModelDetail;
import com.lets.apis.client.generator.creator.ControllerModelClassCreator;

import java.util.Set;
import java.util.stream.Collectors;

public class ControllerModelUtil {

    public static void load(ApiDetail apiDetail) {
        Set<Class> apiClasses = ControllerModelClassCreator.create(apiDetail);
        Set<ControllerModelDetail> controllerModelDetails = apiClasses.stream()
                .map(apiClass -> ControllerModelDetail.create(apiClass))
                .collect(Collectors.toSet());
        apiDetail.setModelDetails(controllerModelDetails);
    }
}