package com.lets.apis.client.generator.util;

import com.lets.apis.client.generator.model.ApiDetail;
import com.lets.apis.client.generator.model.ApiClientDetail;

import java.util.List;
import java.util.Set;

public class ApiClientDetailUtil {

    public static void load(ApiDetail apiDetail) {
        apiDetail.setApiClientDetails(getControllerDetails(apiDetail.getControllerClass()));
    }

    private static List<ApiClientDetail> getControllerDetails(Set<Class> controllerClasses) {
        return controllerClasses.stream()
                .map(controllerClazz -> ApiClientDetail.create(controllerClazz))
                .toList();
    }
}