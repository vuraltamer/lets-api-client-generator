package com.lets.apis.client.generator.util;

import com.lets.apis.client.generator.model.ApiDetail;
import com.lets.apis.client.generator.util.base.Util;
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
        final Reflections reflections = new Reflections(ApiBasePackageUtil.get(), Scanners.TypesAnnotated);
        final Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(RestController.class);
        if (Util.isEmpty(annotatedClasses)) {
            throw new RuntimeException("ControllerReaderUtil::getControllers::controller not found. check scan package");
        }
        return new HashSet<>(annotatedClasses);
    }
}