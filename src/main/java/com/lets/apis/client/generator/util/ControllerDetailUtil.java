package com.lets.apis.client.generator.util;

import com.lets.apis.client.generator.model.ApiDetail;
import com.lets.apis.client.generator.model.ControllerDetail;

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