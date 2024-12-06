package com.lets.apis.api.caller.util;

import com.lets.apis.api.caller.model.ApiDetail;
import com.lets.apis.api.caller.properties.CallerProperties;
import com.lets.apis.api.caller.properties.PropertyReader;
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
        final Reflections reflections = new Reflections(getScanPackage(), Scanners.TypesAnnotated);
        final Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(RestController.class);
        return new HashSet<>(annotatedClasses);
    }

    private static String getScanPackage() {
        CallerProperties properties = PropertyReader.properties();
        return properties.getScanPackage();
    }
}