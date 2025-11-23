package com.lets.apis.client.generator.creator;

import com.lets.apis.client.generator.model.ApiClientDetail;
import com.lets.apis.client.generator.model.node.AnnotationNode;
import org.springframework.web.bind.annotation.RequestMapping;

public class ControllerPathCreator {

    public static String[] create(ApiClientDetail apiClientDetail) {
        if (apiClientDetail.getClazz().isAnnotationPresent(RequestMapping.class)) {
            return getControllerPath(apiClientDetail);
        }
        return new String[]{};
    }

    private static String[] getControllerPath(ApiClientDetail apiClientDetail) {
        RequestMapping annotation = (RequestMapping) apiClientDetail.getClazz().getAnnotation(RequestMapping.class);
        AnnotationNode annotationNode = AnnotationNode.create(annotation);
        return (String[]) annotationNode.getValues().get("value");
    }
}
