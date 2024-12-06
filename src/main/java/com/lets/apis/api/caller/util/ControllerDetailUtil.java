package com.lets.apis.api.caller.util;

import com.lets.apis.api.caller.model.ApiDetail;
import com.lets.apis.api.caller.model.ControllerDetail;

import java.util.List;
import java.util.Set;

public class ControllerDetailUtil {

    public static void load(ApiDetail apiDetail) {
        apiDetail.setControllerDetails(getControllerDetails(apiDetail.getControllerClass()));
    }

    private static List<ControllerDetail> getControllerDetails(Set<Class> controllerClasses) {
        return controllerClasses.stream()
                .map(controllerClazz -> ControllerDetail.create(controllerClazz))
                .toList();
    }
}