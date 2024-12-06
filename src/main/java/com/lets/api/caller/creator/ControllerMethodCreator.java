package com.lets.api.caller.creator;

import com.lets.api.caller.model.ControllerDetail;
import com.lets.api.caller.model.ControllerMethodDetail;
import com.lets.api.caller.model.node.AnnotationNode;
import com.lets.api.caller.model.node.ParameterNode;
import com.lets.api.caller.model.node.ReturnTypeNode;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControllerMethodCreator {

    private static final List<Class<? extends Annotation>> REQUEST_METHOD_ANNOTATIONS = Arrays.asList(
            GetMapping.class,
            PostMapping.class,
            PutMapping.class,
            DeleteMapping.class,
            PatchMapping.class
    );

    public static List<ControllerMethodDetail> create(ControllerDetail controllerDetail) {
        List<ControllerMethodDetail> methodList = new ArrayList<>();
        Method[] methods = controllerDetail.getClazz().getDeclaredMethods();
        for (Method method : methods) {
            Annotation requestMethodAnnotation = findRequestMethodAnnotation(method.getAnnotations());
            if (requestMethodAnnotation != null) {
                methodList.add(
                        ControllerMethodDetail.builder()
                                .method(method)
                                .annotation(AnnotationNode.create(requestMethodAnnotation))
                                .returnType(ReturnTypeNode.create(method))
                                .parameterTypes(ParameterNode.create(method))
                                .build());
            }
        }
        return methodList;
    }

    private static Annotation findRequestMethodAnnotation(Annotation[] annotations) {
        return Arrays.stream(annotations)
                .filter(annotation -> REQUEST_METHOD_ANNOTATIONS.contains(annotation.annotationType()))
                .findAny()
                .orElse(null);
    }
}