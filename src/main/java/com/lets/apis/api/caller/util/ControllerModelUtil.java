package com.lets.apis.api.caller.util;

import com.lets.apis.api.caller.model.ApiDetail;
import com.lets.apis.api.caller.model.ControllerModelDetail;
import com.lets.apis.api.caller.creator.ControllerModelClassCreator;

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