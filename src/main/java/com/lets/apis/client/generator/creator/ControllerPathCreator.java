package com.lets.apis.client.generator.creator;

import com.lets.apis.client.generator.model.ControllerDetail;
import com.lets.apis.client.generator.model.node.AnnotationNode;
import org.springframework.web.bind.annotation.RequestMapping;

public class ControllerPathCreator {

    public static String[] create(ControllerDetail controllerDetail) {
        if (controllerDetail.getClazz().isAnnotationPresent(RequestMapping.class)) {
            return getControllerPath(controllerDetail);
        }
        return new String[]{};
    }

    private static String[] getControllerPath(ControllerDetail controllerDetail) {
        RequestMapping annotation = (RequestMapping) controllerDetail.getClazz().getAnnotation(RequestMapping.class);
        AnnotationNode annotationNode = AnnotationNode.create(annotation);
        return (String[]) annotationNode.getValues().get("value");
    }
}
