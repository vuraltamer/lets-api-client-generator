package com.lets.api.caller.util;

import com.lets.api.caller.model.ApiDetail;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

public class ControllerReaderUtil {

    public static void load(ApiDetail apiDetail) {
        apiDetail.setControllerClass(getControllers());
    }

    public static Set<Class> getControllers() {
        final Reflections reflections = new Reflections("com.project", Scanners.TypesAnnotated);
        final Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(RestController.class);
        return new HashSet<>(annotatedClasses);
    }
}