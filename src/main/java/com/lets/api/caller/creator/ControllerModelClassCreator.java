package com.lets.api.caller.creator;

import com.lets.api.caller.model.ApiDetail;
import com.lets.api.caller.model.ControllerDetail;
import com.lets.api.caller.util.base.Util;
import com.lets.api.caller.model.node.ParameterNode;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lets.api.caller.util.base.Util.isStatic;

public class ControllerModelClassCreator {

    public static Set<Class> create(ApiDetail apiDetail) {
        Set<Class> clazzList = new HashSet<>();
        apiDetail.getControllerDetails().stream()
                .forEach(controllerDetail -> loadApiClasses(controllerDetail, clazzList));
        return clazzList;
    }

    private static void loadApiClasses(ControllerDetail controllerDetail, Set<Class> clazzList) {
        controllerDetail.getClasses().stream()
                .forEach(clazz -> loadApiClasses(clazz, clazzList));
    }

    private static void loadApiClasses(Class clazz, Set<Class> clazzList) {
        if (Util.isNonControllerClass(clazz) || isContainsClass(clazz, clazzList)) {
            return;
        }
        if (Util.isStatic(clazz) && !clazz.isMemberClass()) {
            return;
        }
        clazzList.add(clazz);
        for (Field declaredField : clazz.getDeclaredFields()) {
            if (declaredField.getGenericType() instanceof ParameterizedType) {
                loadParameterizedApiClasses(clazzList, declaredField);
            }
            loadApiClasses(declaredField.getType(), clazzList);
        }
        if (clazz.getSuperclass() != null && !Util.isNonControllerClass(clazz.getSuperclass())) {
            loadApiClasses(clazz.getSuperclass(), clazzList);
        }
    }

    private static void loadParameterizedApiClasses(Set<Class> clazzList, Field declaredField) {
        Set<Class> parameterizedClasses = getParameterizedClasses((ParameterizedType) declaredField.getGenericType(), clazzList);
        parameterizedClasses.stream()
                .forEach(e -> loadApiClasses(e, clazzList));
    }

    private static Set<Class> getParameterizedClasses(ParameterizedType type, Set<Class> clazzList) {
        ParameterNode parameterNode = ParameterNode.createFromParameterized(type); // parameterized
        Set<Class> classNodeClasses = parameterNode.getClassNodeClasses();
        return classNodeClasses.stream()
                .filter(e -> !isContainsClass(e, clazzList))
                .collect(Collectors.toSet());
    }

    private static boolean isContainsClass(Class searchClazz, Set<Class> clazzList) {
        return clazzList.stream()
                .anyMatch(clazz -> clazz.getName().equals(searchClazz.getName()));
    }
}