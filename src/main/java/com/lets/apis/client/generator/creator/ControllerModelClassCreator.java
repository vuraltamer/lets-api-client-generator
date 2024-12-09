package com.lets.apis.client.generator.creator;

import com.lets.apis.client.generator.model.ApiDetail;
import com.lets.apis.client.generator.model.ControllerDetail;
import com.lets.apis.client.generator.util.base.Util;
import com.lets.apis.client.generator.model.node.ParameterNode;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
            if (declaredField.getType().getComponentType() != null
                    && Util.isEnum(declaredField.getType().getComponentType())) {
                if (!declaredField.getName().equals("$VALUES")) {
                    loadApiClasses(declaredField.getType(), clazzList);
                }
            } else {
                loadApiClasses(declaredField.getType(), clazzList);
            }
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