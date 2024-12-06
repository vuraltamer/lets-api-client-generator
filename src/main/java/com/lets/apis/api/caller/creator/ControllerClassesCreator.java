package com.lets.apis.api.caller.creator;

import com.lets.apis.api.caller.model.ControllerDetail;

import java.util.Set;
import java.util.stream.Collectors;

public class ControllerClassesCreator {

    public static Set<Class> create(ControllerDetail controllerDetail) {
        return controllerDetail.getMethods().stream()
                .flatMap(methodDetail -> methodDetail.getClassNodeClasses().stream())
                .collect(Collectors.toSet());
    }
}